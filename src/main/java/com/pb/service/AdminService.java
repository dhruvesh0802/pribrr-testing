package com.pb.service;

import com.pb.config.GlobalConfiguration;
import com.pb.constants.ResponseMessageConstant;
import com.pb.constants.UserType;
import com.pb.dto.*;
import com.pb.exception.CustomException;
import com.pb.model.*;
import com.pb.repository.*;
import com.pb.response.PaginationResponse;
import com.pb.response.UserResponse;
import com.pb.security.JwtTokenUtils;
import com.pb.utils.BaseUtils;
import com.pb.utils.PaginationUtils;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.modelmapper.TypeToken;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import javax.mail.MessagingException;
import javax.transaction.Transactional;
import java.io.UnsupportedEncodingException;
import java.time.LocalDateTime;
import java.util.*;

@Service
@Slf4j
public class AdminService {

    @Autowired
    private UserRepository userRepository;
    
    @Autowired
    private JwtTokenUtils jwtTokenUtils;
    @Autowired
    private ModelMapper modelMapper;
    @Autowired
    private OTPRepository otpRepository;
    @Autowired
    private EmailService emailService;
    @Autowired
    private EmailTemplateRepository emailTemplateRepository;
    @Autowired
    private StaticPageRepository staticPageRepository;
    @Autowired private BannerRepository bannerRepository;
    @Autowired private DepartmentRepository departmentRepository;
    @Autowired private CategoryRepository categoryRepository;

    @Autowired
    UserService userService;
    @Autowired
    AWSS3Service awss3Service;
    @Autowired
    private GlobalConfiguration globalConfiguration;



    public UserDTO login(UserDTO userDTO) {
        UserEntity userEntity = null;
        if(userDTO.getRole().equals("ADMIN")){
            userEntity = userRepository.findByEmailAndPasswordAndUserType(userDTO.getEmail(), userDTO.getPassword(),userDTO.getRole())
                    .orElseThrow(() -> new CustomException(HttpStatus.NOT_FOUND, ResponseMessageConstant.USER_NOT_FOUND));
        } else {
            userEntity = userRepository.findByEmailAndPasswordAndUserTypeNot(userDTO.getEmail(), userDTO.getPassword(),"ADMIN")
                    .orElseThrow(() -> new CustomException(HttpStatus.NOT_FOUND, ResponseMessageConstant.USER_NOT_FOUND));
        }
        if (!userEntity.getIsActive()){
            return null;
        } else {
            String authenticationToken = jwtTokenUtils.getAuthenticationToken(userEntity,userDTO.getIsRemember());
            UserDTO responseDto = modelMapper.map(userEntity, UserDTO.class);

            responseDto.setAuthToken(authenticationToken);
            responseDto.setPassword(null);

            return responseDto;
        }
    }
    
    public PaginationResponse getAllAdmin(UserFilterDTO userFilterDTO, Pageable pageable) {
        String searchKey = userFilterDTO.getSearchKeyword() != null ? userFilterDTO.getSearchKeyword() : "";
        List<UserResponse> userResponses = new ArrayList<>();
        Page<UserEntity> userEntityPage = userRepository.findAllByUserTypeAndEmailContaining
                (userFilterDTO.getUserType().name(),searchKey, pageable);
        TypeToken<Page<UserResponse>> typeToken = new TypeToken<Page<UserResponse>>() {
        };

        Page<UserResponse> userResponsePage = modelMapper.map(userEntityPage, typeToken.getType());

        return PaginationUtils.getPaginationResponse(userResponsePage, false);

    }
    
    public boolean changeStatusOfUser(Long id) {
        UserEntity userEntity = userRepository.findById(id).orElseThrow(() -> new CustomException(HttpStatus.NOT_FOUND, ResponseMessageConstant.CONTENT_NOT_FOUND));
        if (userEntity.getUserType().equalsIgnoreCase("admin")){
            List<UserEntity> userEntity1 = userRepository.findByUserTypeAndDeletedDateIsNullAndIsActiveTrue("ADMIN");
            if (userEntity1.size()>1){
                userEntity.setIsActive(!userEntity.getIsActive());
                userRepository.save(userEntity);
                return true;
            }
            if (userEntity.getIsActive().equals(false)){
                userEntity.setIsActive(!userEntity.getIsActive());
                userRepository.save(userEntity);
                return true;
            }
            else {
                return false;
            }
        }
        else {
            userEntity.setIsActive(!userEntity.getIsActive());
            userRepository.save(userEntity);
            return true;
        }
    }
    
    public Long deleteUser(Long id) {
        UserEntity userEntity = userRepository.findById(id).orElseThrow(() -> new CustomException(HttpStatus.NOT_FOUND, ResponseMessageConstant.RETAILER_NOT_FOUND));
        userRepository.deleteById(userEntity.getId());
        
        return id;
    }
    
    public UserResponse addUser(UserRegisterDTO userDTO) {
        
        if (!userDTO.getPassword().equals(userDTO.getConfirmPassword()))
            throw new CustomException(HttpStatus.BAD_REQUEST, ResponseMessageConstant.PASSWORD_AND_CONFIRM_PASSWORD_SAME);
        if(userRepository.findByEmailAndDeletedDateIsNull(userDTO.getEmail()).isPresent())
            throw new CustomException(HttpStatus.BAD_REQUEST, ResponseMessageConstant.EMAIL_ALREADY_EXIST);

        UserEntity userEntity = modelMapper.map(userDTO, UserEntity.class);
        if(!userDTO.getUserType().equals(UserType.ADMIN)){
            if(userDTO.getProfilePic() != null) {
                GlobalConfiguration.AmazonS3 amazonS3 = globalConfiguration.getAmazonS3();
                String fileName = awss3Service.generateFileName(userDTO.getProfilePic());
                String filePath = amazonS3.getSubFolderName() + amazonS3.getUploadFolderName() + amazonS3.getProfilePhoto() + fileName;
                userEntity.setProfileUrl(fileName);
                awss3Service.uploadFile(userDTO.getProfilePic(), filePath);
            }
        }
        userEntity = userRepository.save(userEntity);
        // user profile
        try {
            userService.sendVerificationEmail(userEntity.getId());
        } catch (MessagingException e) {
            throw new RuntimeException(e);
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException(e);
        }
        return modelMapper.map(userEntity, UserResponse.class);
    }

    public UserResponse editAdminUser(UserRegisterDTO userDTO) {

        if (!userDTO.getPassword().equals(userDTO.getConfirmPassword()))
            throw new CustomException(HttpStatus.BAD_REQUEST, ResponseMessageConstant.PASSWORD_AND_CONFIRM_PASSWORD_SAME);

        UserEntity userEntity = modelMapper.map(userDTO, UserEntity.class);
        userEntity = userRepository.save(userEntity);
        return modelMapper.map(userEntity, UserResponse.class);
    }
    public void deleteAdminUser(Long userId) {
        UserEntity userEntity = userRepository.findById(userId).orElseThrow(() -> new CustomException(HttpStatus.NOT_FOUND, ResponseMessageConstant.USER_NOT_FOUND));
        userRepository.delete(userEntity);
    }
    
    @Transactional
    public ForgotPasswordOTP sendOTP(ForgotPasswordOTP forgotPasswordOTP) throws MessagingException, UnsupportedEncodingException {
        
        UserEntity userEntity = userRepository.findByEmail(forgotPasswordOTP.getEmail())
                .orElseThrow(() -> new CustomException(HttpStatus.NOT_FOUND, ResponseMessageConstant.USER_NOT_FOUND));
        
        otpRepository.deleteAllByUserId(userEntity.getId());

        int otp = BaseUtils.generateRandomOTP();
        OTPEntity otpEntity = new OTPEntity();
        otpEntity.setOtp(otp);
        otpEntity.setUserEntity(userEntity);
        OTPEntity saveOTPEntity = otpRepository.save(otpEntity);
    
        EmailTemplateEntity forgotEmailTemplate =
                emailTemplateRepository.findByTitle("FORGOT PASSWORD");
    
        String body = forgotEmailTemplate.getContent()
                .replace("{{otp}}",String.valueOf(otpEntity.getOtp()));
    
        emailService.sendEmail(userEntity.getEmail(), forgotEmailTemplate.getSubject(), body);
        
        return modelMapper.map(saveOTPEntity, ForgotPasswordOTP.class);
    }
    
    @Transactional
    public ForgotPasswordOTP verifyOTP(ForgotPasswordOTP forgotPasswordOTP) {
        
        UserEntity userEntity = userRepository.findByEmail( forgotPasswordOTP.getEmail())
                .orElseThrow(() -> new CustomException(HttpStatus.NOT_FOUND, ResponseMessageConstant.USER_NOT_FOUND));
        
        OTPEntity otpEntity = otpRepository.findByUserEntity(userEntity)
                .orElseThrow(() -> new CustomException(HttpStatus.BAD_REQUEST, ResponseMessageConstant.INVALID_OTP));
        
        int currentMinutes = LocalDateTime.now().getMinute();
        int createdDateMinutes = otpEntity.getCreatedDate().getMinute();
        
        if ((currentMinutes - createdDateMinutes) >= 10)
            throw new CustomException(HttpStatus.BAD_REQUEST, ResponseMessageConstant.OTP_EXPIRED);
        
        if (!(userEntity.getEmail().equals(forgotPasswordOTP.getEmail()) &&
                otpEntity.getOtp().equals(forgotPasswordOTP.getOtp()))) {
            throw new CustomException(HttpStatus.BAD_REQUEST, ResponseMessageConstant.INVALID_OTP);
        }
        
        return modelMapper.map(otpEntity, ForgotPasswordOTP.class);
    }
    
    @Transactional
    public ForgotPasswordOTP forgotPassword(ForgotPasswordOTP forgotPasswordOTP) {
        
        UserEntity userEntity = userRepository.findByEmail( forgotPasswordOTP.getEmail())
                .orElseThrow(() -> new CustomException(HttpStatus.NOT_FOUND, ResponseMessageConstant.USER_NOT_FOUND));
        
        if (!forgotPasswordOTP.getPassword().equals(forgotPasswordOTP.getConfirmPassword()))
            throw new CustomException(HttpStatus.BAD_REQUEST, ResponseMessageConstant.PASSWORD_AND_CONFIRM_PASSWORD_SAME);
        
        userEntity.setPassword(forgotPasswordOTP.getPassword());
        UserEntity saveUserEntity = userRepository.save(userEntity);
        
        return modelMapper.map(saveUserEntity, ForgotPasswordOTP.class);
    }
    @Transactional
    public String changePassword(ChangePasswordDTO changePasswordDTO) {

        UserEntity userEntity = userRepository.findByIdAndPassword(changePasswordDTO.getUserId(), changePasswordDTO.getOldPassword())
                .orElseThrow(() -> new CustomException(HttpStatus.NOT_FOUND, ResponseMessageConstant.USER_NOT_FOUND));

        if (!changePasswordDTO.getPassword().equals(changePasswordDTO.getConfirmPassword()))
            throw new CustomException(HttpStatus.BAD_REQUEST, ResponseMessageConstant.PASSWORD_AND_CONFIRM_PASSWORD_SAME);

        userEntity.setPassword(changePasswordDTO.getPassword());
        UserEntity saveUserEntity = userRepository.save(userEntity);

        return null;
    }

    @Transactional
    public String addStaticPage(StaticPageDTO staticPageDTO) {

        StaticPageEntity byPageName = new StaticPageEntity();
        if(staticPageDTO.getId()!= 0){
            byPageName = staticPageRepository.findById(staticPageDTO.getId()).get();
            byPageName.setContent(staticPageDTO.getContent());
        } else {
            byPageName = new StaticPageEntity();
            byPageName.setPageName(staticPageDTO.getPageName());
            byPageName.setContent(staticPageDTO.getContent());
        }
        staticPageRepository.save(byPageName);

        return staticPageDTO.getContent();
    }

    public PaginationResponse getAllStaticPage(Pageable pageable) {
        Page<StaticPageEntity> all = staticPageRepository.findAll(pageable);
        Page<StaticPageEntity> userResponsePage = all;
        return PaginationUtils.getPaginationResponse(userResponsePage, false);
    }
    public void deleteStaticPage(Long id) {
        staticPageRepository.deleteById(id);
    }
    public Map<String,Long> dashboard() {
        Map<String,Long> res = new HashMap<>();
        res.put("Retailer",userRepository.countByUserTypeAndDeletedDateIsNull("RETAILER"));
        res.put("Supplier",userRepository.countByUserTypeAndDeletedDateIsNull("SUPPLIER"));
        res.put("Admin",userRepository.countByUserTypeAndDeletedDateIsNull("ADMIN"));
        res.put("Banner",bannerRepository.count());
        res.put("Department",departmentRepository.count());
        res.put("Categories",categoryRepository.count());
        return res;
    }
}
