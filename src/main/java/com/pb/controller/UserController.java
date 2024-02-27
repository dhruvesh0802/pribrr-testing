package com.pb.controller;

import com.pb.constants.APIEndpointConstant;
import com.pb.constants.ResponseMessageConstant;
import com.pb.dto.CategoryDTO;
import com.pb.dto.ChangePasswordDTO;
import com.pb.dto.UserDTO;
import com.pb.dto.profile.*;
import com.pb.dto.profile.response.*;
import com.pb.exception.CustomException;
import com.pb.repository.UserRepository;
import com.pb.response.CustomResponse;
import com.pb.service.AdminService;
import com.pb.service.CategoryService;
import com.pb.service.ProfileService;
import com.pb.service.UserService;
import com.pb.utils.Constant;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.Authorization;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.io.UnsupportedEncodingException;
import java.util.List;
import java.util.Map;
import java.util.Properties;

@RestController
@RequestMapping(value = APIEndpointConstant.USER_URLS.BASE_URL)
@Slf4j
@Api(value = APIEndpointConstant.USER_URLS.BASE_URL, tags = "User")
@CrossOrigin
public class UserController {
    @Autowired
    private CategoryService categoryService;

    @Autowired
    private ProfileService profileService;

    @Autowired private AdminService adminService;
    @Autowired private UserService userService;

    @Autowired
    private UserRepository userRepository;

    @GetMapping
    @ApiOperation(value = "Get User By Token", response = Object.class, authorizations = {@Authorization(value = Constant.API_KEY)})
    public ResponseEntity<CustomResponse> getUser(HttpServletRequest req) {
        try {
            UserDTO user = userService.getUser(req.getHeader(Constant.ACCESS_TOKEN));
            return new ResponseEntity<>(new CustomResponse(HttpStatus.OK, ResponseMessageConstant.USER_FETCHED_SUCCESSFULLY, user), HttpStatus.OK);
        } catch (CustomException e) {
            log.error(e.getMessage(), e);
            return new ResponseEntity<>(new CustomResponse(e.getHttpStatus(), e.getMessage()), e.getHttpStatus());
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            return new ResponseEntity<>(new CustomResponse(HttpStatus.INTERNAL_SERVER_ERROR, ResponseMessageConstant.SOMETHING_WENT_WRONG), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    @GetMapping(value = APIEndpointConstant.CATEGORY_URLS.BASE_URL+"/"+APIEndpointConstant.CATEGORY_URLS.GET_CATEGORY_BY_DEPARTMENT_ID+"/{departmentId}")
    @ApiOperation(value = "Get All Category By Department Id", response = Object.class, authorizations = {@Authorization(value = Constant.API_KEY)})
    
    public ResponseEntity<CustomResponse> getAllCategoryByDepartmentId(@PathVariable Long departmentId) {
        try {
            List<CategoryDTO> categoryResponse = categoryService.getAllCategoryByDepartmentId(departmentId);
            return new ResponseEntity<>(new CustomResponse(HttpStatus.OK, ResponseMessageConstant.CATEGORY_FETCH_SUCCESSFULLY, categoryResponse), HttpStatus.OK);
        } catch (CustomException e) {
            log.error(e.getMessage(), e);
            return new ResponseEntity<>(new CustomResponse(e.getHttpStatus(), e.getMessage()), e.getHttpStatus());
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            return new ResponseEntity<>(new CustomResponse(HttpStatus.INTERNAL_SERVER_ERROR, ResponseMessageConstant.SOMETHING_WENT_WRONG), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping(value = APIEndpointConstant.USER_URLS.CHANGE_PASSWORD)
    @ApiOperation(value = "Change password", response = Object.class, authorizations = {@Authorization(value = Constant.API_KEY)})
    public ResponseEntity<?> changePassword(@Valid @RequestBody ChangePasswordDTO changePasswordDTO) {
        try {
            String res = adminService.changePassword(changePasswordDTO);
            return new ResponseEntity<>(new CustomResponse(HttpStatus.OK, ResponseMessageConstant.PASSWORD_CHANGED_SUCCESSFULLY, res), HttpStatus.OK);
        } catch (CustomException e) {
            log.error(e.getMessage(), e);
            return new ResponseEntity<>(new CustomResponse(e.getHttpStatus(), e.getMessage()), e.getHttpStatus());
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            return new ResponseEntity<>(new CustomResponse(HttpStatus.INTERNAL_SERVER_ERROR, ResponseMessageConstant.SOMETHING_WENT_WRONG), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping(value = APIEndpointConstant.PROFILE_URLS.BASE_URL+APIEndpointConstant.PROFILE_URLS.COMPANY_DETAILS)
    @ApiOperation(value = "Update Profile Company Details", response = Object.class, authorizations = {@Authorization(value = Constant.API_KEY)})
    public ResponseEntity<?> updateProfileCompanyDetails(@Valid @ModelAttribute CompanyDetailsDTO companyDetailsDTO) {
        try {
            Integer completed = profileService.addProfileCompanyDetails(companyDetailsDTO);
            return new ResponseEntity<>(new CustomResponse(HttpStatus.OK, ResponseMessageConstant.PROFILE_UPDATED_SUCCESSFULLY, completed), HttpStatus.OK);
        } catch (CustomException e) {
            log.error(e.getMessage(), e);
            return new ResponseEntity<>(new CustomResponse(e.getHttpStatus(), e.getMessage()), e.getHttpStatus());
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            return new ResponseEntity<>(new CustomResponse(HttpStatus.INTERNAL_SERVER_ERROR, ResponseMessageConstant.SOMETHING_WENT_WRONG), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping(value = APIEndpointConstant.PROFILE_URLS.BASE_URL+APIEndpointConstant.PROFILE_URLS.MEMBER_DETAILS)
    @ApiOperation(value = "Update Profile Member Details", response = Object.class, authorizations = {@Authorization(value = Constant.API_KEY)})
    public ResponseEntity<?> updateProfileMemberDetails(@Valid @ModelAttribute MemberDetailsDTO memberDetailsDTO) {
        try {
            Integer completed = profileService.addProfileMemberDetails(memberDetailsDTO);
            return new ResponseEntity<>(new CustomResponse(HttpStatus.OK, ResponseMessageConstant.PROFILE_UPDATED_SUCCESSFULLY, completed), HttpStatus.OK);
        } catch (CustomException e) {
            log.error(e.getMessage(), e);
            return new ResponseEntity<>(new CustomResponse(e.getHttpStatus(), e.getMessage()), e.getHttpStatus());
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            return new ResponseEntity<>(new CustomResponse(HttpStatus.INTERNAL_SERVER_ERROR, ResponseMessageConstant.SOMETHING_WENT_WRONG), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping(value = APIEndpointConstant.PROFILE_URLS.BASE_URL+APIEndpointConstant.PROFILE_URLS.PRODUCT_DETAILS)
    @ApiOperation(value = "Update Profile Product Details", response = Object.class, authorizations = {@Authorization(value = Constant.API_KEY)})
    public ResponseEntity<?> updateProfileProductDetails(@Valid @ModelAttribute ProductDetailsDTO productDetailsDTO) {
        try {
            Integer completed = profileService.addProfileProductDetails(productDetailsDTO);
            return new ResponseEntity<>(new CustomResponse(HttpStatus.OK, ResponseMessageConstant.PROFILE_UPDATED_SUCCESSFULLY, completed), HttpStatus.OK);
        } catch (CustomException e) {
            log.error(e.getMessage(), e);
            return new ResponseEntity<>(new CustomResponse(e.getHttpStatus(), e.getMessage()), e.getHttpStatus());
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            return new ResponseEntity<>(new CustomResponse(HttpStatus.INTERNAL_SERVER_ERROR, ResponseMessageConstant.SOMETHING_WENT_WRONG), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping(value = APIEndpointConstant.PROFILE_URLS.BASE_URL+APIEndpointConstant.PROFILE_URLS.ARTWORK_DETAILS)
    @ApiOperation(value = "Update Profile ArtWork Details", response = Object.class, authorizations = {@Authorization(value = Constant.API_KEY)})
    public ResponseEntity<?> updateProfileArtWorkDetails(@Valid @ModelAttribute ArtworkDetailsDTO artworkDetailsDTO) {
        try {
            Integer completed = profileService.addProfileArtworkDetails(artworkDetailsDTO);
            return new ResponseEntity<>(new CustomResponse(HttpStatus.OK, ResponseMessageConstant.PROFILE_UPDATED_SUCCESSFULLY, completed), HttpStatus.OK);
        } catch (CustomException e) {
            log.error(e.getMessage(), e);
            return new ResponseEntity<>(new CustomResponse(e.getHttpStatus(), e.getMessage()), e.getHttpStatus());
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            return new ResponseEntity<>(new CustomResponse(HttpStatus.INTERNAL_SERVER_ERROR, ResponseMessageConstant.SOMETHING_WENT_WRONG), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping(value = APIEndpointConstant.PROFILE_URLS.BASE_URL+APIEndpointConstant.PROFILE_URLS.QA_DETAILS)
    @ApiOperation(value = "Update Profile QA Details", response = Object.class, authorizations = {@Authorization(value = Constant.API_KEY)})
    public ResponseEntity<?> updateProfileQADetails(@Valid @ModelAttribute QADetailsDTO qaDetailsDTO) {
        try {
            Integer completed = profileService.addProfileQADetails(qaDetailsDTO);
            return new ResponseEntity<>(new CustomResponse(HttpStatus.OK, ResponseMessageConstant.PROFILE_UPDATED_SUCCESSFULLY, completed), HttpStatus.OK);
        } catch (CustomException e) {
            log.error(e.getMessage(), e);
            return new ResponseEntity<>(new CustomResponse(e.getHttpStatus(), e.getMessage()), e.getHttpStatus());
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            return new ResponseEntity<>(new CustomResponse(HttpStatus.INTERNAL_SERVER_ERROR, ResponseMessageConstant.SOMETHING_WENT_WRONG), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping(value = APIEndpointConstant.PROFILE_URLS.BASE_URL+APIEndpointConstant.PROFILE_URLS.GET_PERCENTAGE+"/{userId}")
    @ApiOperation(value = "Get Profile Complete Percentage", response = Object.class, authorizations = {@Authorization(value = Constant.API_KEY)})
    public ResponseEntity<CustomResponse> getProfileCompletePercentage(@PathVariable Long userId) {
        try {
            Map<String, String> userProfileCompletePercentage = profileService.getUserProfileCompletePercentageMap(userId);
            return new ResponseEntity<>(new CustomResponse(HttpStatus.OK, ResponseMessageConstant.CATEGORY_FETCH_SUCCESSFULLY, userProfileCompletePercentage), HttpStatus.OK);
        } catch (CustomException e) {
            log.error(e.getMessage(), e);
            return new ResponseEntity<>(new CustomResponse(e.getHttpStatus(), e.getMessage()), e.getHttpStatus());
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            return new ResponseEntity<>(new CustomResponse(HttpStatus.INTERNAL_SERVER_ERROR, ResponseMessageConstant.SOMETHING_WENT_WRONG), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping(value = APIEndpointConstant.PROFILE_URLS.BASE_URL+APIEndpointConstant.PROFILE_URLS.COMPANY_DETAILS+"/{userId}")
    @ApiOperation(value = "Get Profile Company Details", response = Object.class, authorizations = {@Authorization(value = Constant.API_KEY)})
    public ResponseEntity<?> getProfileCompanyDetails(@PathVariable Long userId) {
        try {
            CompanyDetailsResDTO profileCompanyDetails = profileService.getProfileCompanyDetails(userId);
            return new ResponseEntity<>(new CustomResponse(HttpStatus.OK, ResponseMessageConstant.PROFILE_UPDATED_SUCCESSFULLY, profileCompanyDetails), HttpStatus.OK);
        } catch (CustomException e) {
            log.error(e.getMessage(), e);
            return new ResponseEntity<>(new CustomResponse(e.getHttpStatus(), e.getMessage()), e.getHttpStatus());
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            return new ResponseEntity<>(new CustomResponse(HttpStatus.INTERNAL_SERVER_ERROR, ResponseMessageConstant.SOMETHING_WENT_WRONG), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping(value = APIEndpointConstant.PROFILE_URLS.BASE_URL+APIEndpointConstant.PROFILE_URLS.MEMBER_DETAILS+"/{userId}")
    @ApiOperation(value = "Get Profile Member Details", response = Object.class, authorizations = {@Authorization(value = Constant.API_KEY)})
    public ResponseEntity<?> getProfileMemberDetails(@PathVariable Long userId) {
        try {
            MemberDetailsResDTO profileMemberDetails = profileService.getProfileMemberDetails(userId);
            return new ResponseEntity<>(new CustomResponse(HttpStatus.OK, ResponseMessageConstant.PROFILE_UPDATED_SUCCESSFULLY, profileMemberDetails), HttpStatus.OK);
        } catch (CustomException e) {
            log.error(e.getMessage(), e);
            return new ResponseEntity<>(new CustomResponse(e.getHttpStatus(), e.getMessage()), e.getHttpStatus());
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            return new ResponseEntity<>(new CustomResponse(HttpStatus.INTERNAL_SERVER_ERROR, ResponseMessageConstant.SOMETHING_WENT_WRONG), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping(value = APIEndpointConstant.PROFILE_URLS.BASE_URL+APIEndpointConstant.PROFILE_URLS.PRODUCT_DETAILS+"/{userId}")
    @ApiOperation(value = "Get Profile Product Details", response = Object.class, authorizations = {@Authorization(value = Constant.API_KEY)})
    public ResponseEntity<?> getProfileProductDetails(@PathVariable Long userId) {
        try {
            ProductDetailsResDTO profileProductDetails = profileService.getProfileProductDetails(userId);
            return new ResponseEntity<>(new CustomResponse(HttpStatus.OK, ResponseMessageConstant.PROFILE_UPDATED_SUCCESSFULLY, profileProductDetails), HttpStatus.OK);
        } catch (CustomException e) {
            log.error(e.getMessage(), e);
            return new ResponseEntity<>(new CustomResponse(e.getHttpStatus(), e.getMessage()), e.getHttpStatus());
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            return new ResponseEntity<>(new CustomResponse(HttpStatus.INTERNAL_SERVER_ERROR, ResponseMessageConstant.SOMETHING_WENT_WRONG), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping(value = APIEndpointConstant.PROFILE_URLS.BASE_URL+APIEndpointConstant.PROFILE_URLS.ARTWORK_DETAILS+"/{userId}")
    @ApiOperation(value = "Get Profile ArtWork Details", response = Object.class, authorizations = {@Authorization(value = Constant.API_KEY)})
    public ResponseEntity<?> getProfileArtWorkDetails(@PathVariable Long userId) {
        try {
            ArtworkDetailsResDTO profileArtWorkDetails = profileService.getProfileArtWorkDetails(userId);
            return new ResponseEntity<>(new CustomResponse(HttpStatus.OK, ResponseMessageConstant.PROFILE_UPDATED_SUCCESSFULLY, profileArtWorkDetails), HttpStatus.OK);
        } catch (CustomException e) {
            log.error(e.getMessage(), e);
            return new ResponseEntity<>(new CustomResponse(e.getHttpStatus(), e.getMessage()), e.getHttpStatus());
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            return new ResponseEntity<>(new CustomResponse(HttpStatus.INTERNAL_SERVER_ERROR, ResponseMessageConstant.SOMETHING_WENT_WRONG), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping(value = APIEndpointConstant.PROFILE_URLS.BASE_URL+APIEndpointConstant.PROFILE_URLS.QA_DETAILS+"/{userId}")
    @ApiOperation(value = "Get Profile QA Details", response = Object.class, authorizations = {@Authorization(value = Constant.API_KEY)})
    public ResponseEntity<?> getProfileQADetails(@PathVariable Long userId) {
        try {
            QADetailsResDTO profileQADetails = profileService.getProfileQADetails(userId);
            return new ResponseEntity<>(new CustomResponse(HttpStatus.OK, ResponseMessageConstant.PROFILE_UPDATED_SUCCESSFULLY, profileQADetails), HttpStatus.OK);
        } catch (CustomException e) {
            log.error(e.getMessage(), e);
            return new ResponseEntity<>(new CustomResponse(e.getHttpStatus(), e.getMessage()), e.getHttpStatus());
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            return new ResponseEntity<>(new CustomResponse(HttpStatus.INTERNAL_SERVER_ERROR, ResponseMessageConstant.SOMETHING_WENT_WRONG), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    @PostMapping(value = APIEndpointConstant.USER_URLS.ADD_TO_NETWORK)
    public ResponseEntity<?> addUserContact(@RequestBody Map<String, Long> userIds) {
        try {
            String res = userService.addToNetwork(userIds.get("userId"), userIds.get("networkUserId"));
            return new ResponseEntity<>(new CustomResponse(HttpStatus.OK, res, res), HttpStatus.OK);
        } catch (CustomException e) {
            log.error(e.getMessage(), e);
            return new ResponseEntity<>(new CustomResponse(e.getHttpStatus(), e.getMessage()), e.getHttpStatus());
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            return new ResponseEntity<>(new CustomResponse(HttpStatus.INTERNAL_SERVER_ERROR, ResponseMessageConstant.SOMETHING_WENT_WRONG), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    @GetMapping("/send-verify-email")
    @ApiOperation(value = "Send Verify Email", response = Object.class, authorizations = {@Authorization(value = Constant.API_KEY)})
    public ResponseEntity<CustomResponse> sendVerifyEmail(HttpServletRequest req) {
        try {
            userService.resendVerificationEmail(req.getHeader(Constant.ACCESS_TOKEN));
            return new ResponseEntity<>(new CustomResponse(HttpStatus.OK, ResponseMessageConstant.LINK_SENT_SUCCESSFULLY, null), HttpStatus.OK);
        } catch (CustomException e) {
            log.error(e.getMessage(), e);
            return new ResponseEntity<>(new CustomResponse(e.getHttpStatus(), e.getMessage()), e.getHttpStatus());
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            return new ResponseEntity<>(new CustomResponse(HttpStatus.INTERNAL_SERVER_ERROR, ResponseMessageConstant.SOMETHING_WENT_WRONG), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    @GetMapping("/verify-email")
    @ApiOperation(value = "Verify Email", response = Object.class, authorizations = {@Authorization(value = Constant.API_KEY)})
    public ResponseEntity<CustomResponse> VerifyEmail(String token) {
        try {
            UserDTO userDTO = userService.verifyEmail(token);
            return new ResponseEntity<>(new CustomResponse(HttpStatus.OK, ResponseMessageConstant.EMAIL_VERIFIED_SUCCESSFULLY, userDTO), HttpStatus.OK);
        } catch (CustomException e) {
            log.error(e.getMessage(), e);
            return new ResponseEntity<>(new CustomResponse(e.getHttpStatus(), e.getMessage()), e.getHttpStatus());
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            return new ResponseEntity<>(new CustomResponse(HttpStatus.INTERNAL_SERVER_ERROR, ResponseMessageConstant.SOMETHING_WENT_WRONG), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping(value = "/send")
    public void VerifyEmail() throws UnsupportedEncodingException, MessagingException {

            Properties props = System.getProperties();
            props.put("mail.transport.protocol", "smtp");
            props.put("mail.smtp.port", 587);
            props.put("mail.smtp.starttls.enable", "true");
            props.put("mail.smtp.auth", "true");
            Session session = Session.getDefaultInstance(props);
            MimeMessage msg = new MimeMessage(session);
            msg.setFrom(new InternetAddress("pribrllc@gmail.com",""));
            msg.setRecipient(Message.RecipientType.TO, new InternetAddress("sahil.patel@openxcell.com"));
            msg.setSubject("test");
            msg.setContent("test mnesssss","text/html");

            Transport transport = session.getTransport();
            try
            {
                transport.connect("email-smtp.us-east-1.amazonaws.com","AKIA3KZ2KZJVAQKWICN7","BFW2sglP2ylkHtrVNvc6TVX1CwyZtxLg5/4Zl9up8bWS");
                transport.sendMessage(msg, msg.getAllRecipients());

                System.out.println("Email Send Successfully.");
            }
            catch (Exception ex) {
                System.out.println("Error message: " + ex.getMessage());
            }
            finally
            {
                transport.close();
            }

    }
}
