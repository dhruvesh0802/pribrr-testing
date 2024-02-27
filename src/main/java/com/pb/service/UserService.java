package com.pb.service;

import com.pb.config.GlobalConfiguration;
import com.pb.constants.ResponseMessageConstant;
import com.pb.dto.ProductDTO;
import com.pb.dto.ProductResDTO;
import com.pb.dto.UserDTO;
import com.pb.exception.CustomException;
import com.pb.model.*;
import com.pb.repository.*;
import com.pb.response.PaginationResponse;
import com.pb.security.JwtTokenUtils;
import com.pb.utils.Constant;
import com.pb.utils.PaginationUtils;
import lombok.extern.slf4j.Slf4j;
import ma.glasnost.orika.MapperFacade;
import org.modelmapper.ModelMapper;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.mail.MessagingException;
import javax.transaction.Transactional;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@Slf4j
public class UserService {

    @Autowired
    private AWSS3Service awss3Service;
    @Autowired
    private JwtTokenUtils jwtTokenUtils;
    @Autowired
    private GlobalConfiguration globalConfiguration;
    @Autowired
    private ModelMapper modelMapper;
    private final MapperFacade mapperFacade;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private JwtTokenUtils jwtTokenUtil;
    @Autowired
    private ProfileRepository profileRepository;
    @Autowired
    private EmailTemplateRepository emailTemplateRepository;
    @Autowired
    private EmailService emailService;

    @Value("${mail.url}")
    private String mailUrl;
    UserService(MapperFacade mapperFacade){
        this.mapperFacade =mapperFacade;
    }


    public String addToNetwork(Long id,Long networkId) {
        UserEntity user = userRepository.findById(id).orElseThrow(() -> new RuntimeException("User not found"));
        UserEntity networkUser = userRepository.findById(networkId).orElseThrow(() -> new RuntimeException("Contact not found"));
        String msg = "";
        Set<UserEntity> network = user.getNetwork();
        if(user.getNetwork().stream().filter(obj -> obj.getId().equals(networkId)).count()>0){
            user.getNetwork().remove(networkUser);
            msg = "USER REMOVED FROM NETWORK...!";
        } else {
            user.getNetwork().add(networkUser);
            msg = "USER ADDED IN NETWORK...!";
        }
        //networkUser.getFriends().add(user);
        userRepository.save(user);
        return msg;
        //serRepository.save(networkUser);
    }

    public UserDTO getUser( String header) {
        String userEmail = jwtTokenUtil.getUsernameFromToken(header);
        Optional<UserEntity> byEmail = userRepository.findByEmail(userEmail);
        UserDTO responseDto = new UserDTO();
        if(byEmail.isPresent()){
            UserEntity userEntity = byEmail.get();
            responseDto = modelMapper.map(userEntity, UserDTO.class);
            responseDto.setPassword(null);
            GlobalConfiguration.AmazonS3 amazonS3 = globalConfiguration.getAmazonS3();
            String filePath = amazonS3.getS3BaseUrl() + amazonS3.getUploadFolderName()+ amazonS3.getProfilePhoto() + userEntity.getProfileUrl();
            responseDto.setProfilePath(filePath);
        }

        return responseDto;
    }

    public void resendVerificationEmail(String header) throws MessagingException, UnsupportedEncodingException {
        String userEmail = jwtTokenUtil.getUsernameFromToken(header);
        UserEntity userEntity = userRepository.findByEmail(userEmail).orElseThrow(() -> new CustomException(HttpStatus.NOT_FOUND, ResponseMessageConstant.USER_NOT_FOUND));
        sendVerificationEmail(userEntity.getId());
    }
    public void sendVerificationEmail(Long userId) throws MessagingException, UnsupportedEncodingException {
        UserEntity userEntity = userRepository.findById(userId).orElseThrow(() -> new CustomException(HttpStatus.NOT_FOUND, ResponseMessageConstant.USER_NOT_FOUND));
        EmailTemplateEntity verifyEmailTemplate =
                emailTemplateRepository.findByTitle("VERIFY EMAIL");
        String authenticationToken = jwtTokenUtils.getAuthenticationTokenEmailVerification(userEntity);
        String body = verifyEmailTemplate.getContent()
                .replace("{{url}}",mailUrl+authenticationToken);
        emailService.sendEmail(userEntity.getEmail(), verifyEmailTemplate.getSubject(), body);

    }
    public UserDTO verifyEmail(String token) throws MessagingException, UnsupportedEncodingException {
        UserDTO userDTO = new UserDTO();
        try {
            Long userId = Long.parseLong(jwtTokenUtil.getUserIdFromToken(token));
            Optional<UserEntity> byId = userRepository.findById(userId);
            if(byId.isPresent()){
                UserEntity userEntity = byId.get();
                userEntity.setIsVerified(true);
                UserEntity save = userRepository.save(userEntity);
                userDTO.setEmail(save.getEmail());
                userDTO.setPassword(save.getPassword());
                userDTO.setIsRemember(false);
                userDTO = userLogin(userDTO);
            }
        } catch (Exception e){
            throw new CustomException(HttpStatus.NOT_ACCEPTABLE, ResponseMessageConstant.INVALID_JWT_TOKEN);
        }



        return userDTO;
    }

    public UserDTO userLogin(UserDTO userDTO){
        UserEntity userEntity = userRepository.findByEmailAndPasswordAndUserTypeNot(userDTO.getEmail(), userDTO.getPassword(),"ADMIN")
                .orElseThrow(() -> new CustomException(HttpStatus.NOT_FOUND, ResponseMessageConstant.USER_NOT_FOUND));
        if (!userEntity.getIsActive()){
            return null;
        }else {
            String authenticationToken = jwtTokenUtils.getAuthenticationToken(userEntity,userDTO.getIsRemember());
            UserDTO responseDto = modelMapper.map(userEntity, UserDTO.class);
            responseDto.setAuthToken(authenticationToken);
            responseDto.setPassword(null);

            return responseDto;
        }
    }
}
