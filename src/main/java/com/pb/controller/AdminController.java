package com.pb.controller;

import com.pb.constants.APIEndpointConstant;
import com.pb.constants.ResponseMessageConstant;
import com.pb.dto.*;
import com.pb.exception.CustomException;
import com.pb.response.CustomResponse;
import com.pb.response.PaginationResponse;
import com.pb.response.UserResponse;
import com.pb.service.AdminService;
import com.pb.utils.Constant;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.Authorization;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.Map;
import java.util.Objects;

@Service
@RequestMapping(value = APIEndpointConstant.ADMIN_URLS.BASE_URL)
@Slf4j
@Api(value = APIEndpointConstant.ADMIN_URLS.BASE_URL,
        tags = "Admin")
@CrossOrigin
//@Validated
public class AdminController {
    
    @Autowired
    private AdminService adminService;
    @Autowired
    private JavaMailSender mailSender;

    @PostMapping(value = APIEndpointConstant.COMMON_URL.LOGIN)
    public ResponseEntity<?> login(@Valid @RequestBody UserDTO userDTO) {
        try {
            userDTO.setRole("ADMIN");
            UserDTO userDTO1 = adminService.login(userDTO);
            if (Objects.nonNull(userDTO1)) {
                return new ResponseEntity<>(
                        new CustomResponse(HttpStatus.OK, ResponseMessageConstant.ADMIN_LOGIN_SUCCESSFULLY, userDTO1),
                        HttpStatus.OK);
            }
            else {
                return new ResponseEntity<>(
                        new CustomResponse(HttpStatus.OK, ResponseMessageConstant.ADMIN_CANT_LOGIN, null),
                        HttpStatus.OK);
            }
        } catch (CustomException e) {
            log.error(e.getMessage(), e);
            return new ResponseEntity<>(new CustomResponse(e.getHttpStatus(), e.getMessage()), e.getHttpStatus());
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            return new ResponseEntity<>(new CustomResponse(HttpStatus.INTERNAL_SERVER_ERROR, ResponseMessageConstant.SOMETHING_WENT_WRONG), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    
    @PostMapping(value = APIEndpointConstant.ADMIN_URLS.GET_ALL_ADMIN)
    public ResponseEntity<?> getAllAdmin(@RequestBody UserFilterDTO userFilterDTO, @PageableDefault(size = 10, sort = "created_date", direction = Sort.Direction.DESC) Pageable pageable) {
        try {
            PaginationResponse userResponseList = adminService.getAllAdmin(userFilterDTO, pageable);
            return new ResponseEntity<>(
                    new CustomResponse(HttpStatus.OK, ResponseMessageConstant.USERS_FETCHED_SUCCESSFULLY, userResponseList), HttpStatus.OK);
        } catch (CustomException e) {
            log.error(e.getMessage(), e);
            return new ResponseEntity<>(new CustomResponse(e.getHttpStatus(), e.getMessage()), e.getHttpStatus());
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            return new ResponseEntity<>(new CustomResponse(HttpStatus.INTERNAL_SERVER_ERROR, ResponseMessageConstant.SOMETHING_WENT_WRONG), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    
    @PostMapping(value = APIEndpointConstant.COMMON_URL.REGISTER)
    public ResponseEntity<?> registerUser(@Valid @RequestBody UserRegisterDTO userRegisterDTO) {
        try {
            UserResponse save = adminService.addUser(userRegisterDTO);
            return new ResponseEntity<>(new CustomResponse(HttpStatus.OK, ResponseMessageConstant.USER_REGISTRATION_SUCCESSFULLY, save), HttpStatus.OK);
        } catch (CustomException e) {
            log.error(e.getMessage(), e);
            return new ResponseEntity<>(new CustomResponse(e.getHttpStatus(), e.getMessage()), e.getHttpStatus());
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            return new ResponseEntity<>(new CustomResponse(HttpStatus.INTERNAL_SERVER_ERROR, ResponseMessageConstant.SOMETHING_WENT_WRONG), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    @PostMapping(value = APIEndpointConstant.ADMIN_URLS.EDIT_ADMIN)
    public ResponseEntity<?> editAdminUser(@Valid @RequestBody UserRegisterDTO userRegisterDTO) {
        try {
            UserResponse save = adminService.editAdminUser(userRegisterDTO);
            return new ResponseEntity<>(new CustomResponse(HttpStatus.OK, ResponseMessageConstant.USER_UPDATED_SUCCESSFULLY, save), HttpStatus.OK);
        } catch (CustomException e) {
            log.error(e.getMessage(), e);
            return new ResponseEntity<>(new CustomResponse(e.getHttpStatus(), e.getMessage()), e.getHttpStatus());
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            return new ResponseEntity<>(new CustomResponse(HttpStatus.INTERNAL_SERVER_ERROR, ResponseMessageConstant.SOMETHING_WENT_WRONG), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @DeleteMapping(value = APIEndpointConstant.ADMIN_URLS.DELETE_ADMIN + "/{userId}")
    @ApiOperation(value = "Delete Admin user", response = Object.class, authorizations = {@Authorization(value = Constant.API_KEY)})
    public ResponseEntity<?> deleteAdminUser(@PathVariable Long userId) {
        try {
            adminService.deleteAdminUser(userId);
            return new ResponseEntity<>(new CustomResponse(HttpStatus.OK, ResponseMessageConstant.USER_DELETED_SUCCESSFULLY, null), HttpStatus.OK);
        } catch (CustomException e) {
            log.error(e.getMessage(), e);
            return new ResponseEntity<>(new CustomResponse(e.getHttpStatus(), e.getMessage()), e.getHttpStatus());
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            return new ResponseEntity<>(new CustomResponse(HttpStatus.INTERNAL_SERVER_ERROR, ResponseMessageConstant.SOMETHING_WENT_WRONG), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    
    @PostMapping(value = APIEndpointConstant.ADMIN_URLS.SEND_OTP)
    public ResponseEntity<?> sendOTP(@Valid @RequestBody ForgotPasswordOTP forgotPasswordOTP) {
        try {
            ForgotPasswordOTP forgotPasswordOTP1 = adminService.sendOTP(forgotPasswordOTP);
            
            return new ResponseEntity<>(new CustomResponse(HttpStatus.OK, ResponseMessageConstant.OTP_SEND_SUCCESSFULLY, null), HttpStatus.OK);
        } catch (CustomException e) {
            log.error(e.getMessage(), e);
            return new ResponseEntity<>(new CustomResponse(e.getHttpStatus(), e.getMessage()), e.getHttpStatus());
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            return new ResponseEntity<>(new CustomResponse(HttpStatus.INTERNAL_SERVER_ERROR, ResponseMessageConstant.SOMETHING_WENT_WRONG), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    
    @PostMapping(value = APIEndpointConstant.ADMIN_URLS.VERIFY_OTP)
    public ResponseEntity<?> verifyOTP(@Valid @RequestBody ForgotPasswordOTP forgotPasswordOTP) {
        try {
            ForgotPasswordOTP forgotPasswordOTP1 = adminService.verifyOTP(forgotPasswordOTP);
            return new ResponseEntity<>(new CustomResponse(HttpStatus.OK, ResponseMessageConstant.OTP_VERIFY_SUCCESSFULLY, forgotPasswordOTP1), HttpStatus.OK);
        } catch (CustomException e) {
            log.error(e.getMessage(), e);
            return new ResponseEntity<>(new CustomResponse(e.getHttpStatus(), e.getMessage()), e.getHttpStatus());
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            return new ResponseEntity<>(new CustomResponse(HttpStatus.INTERNAL_SERVER_ERROR, ResponseMessageConstant.SOMETHING_WENT_WRONG), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping(value = APIEndpointConstant.ADMIN_URLS.FORGOT_PASSWORD)
    public ResponseEntity<?> forgotPassword(@Valid @RequestBody ForgotPasswordOTP forgotPasswordOTP) {
        try {
            ForgotPasswordOTP forgotPasswordOTP1 = adminService.forgotPassword(forgotPasswordOTP);
            return new ResponseEntity<>(new CustomResponse(HttpStatus.OK, ResponseMessageConstant.PASSWORD_CHANGED_SUCCESSFULLY, forgotPasswordOTP1), HttpStatus.OK);
        } catch (CustomException e) {
            log.error(e.getMessage(), e);
            return new ResponseEntity<>(new CustomResponse(e.getHttpStatus(), e.getMessage()), e.getHttpStatus());
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            return new ResponseEntity<>(new CustomResponse(HttpStatus.INTERNAL_SERVER_ERROR, ResponseMessageConstant.SOMETHING_WENT_WRONG), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping(value = APIEndpointConstant.ADMIN_URLS.ADD_STATIC_PAGE)
    @ApiOperation(value = "Add static page", response = Object.class, authorizations = {@Authorization(value = Constant.API_KEY)})
    public ResponseEntity<?> addStaticPage(@Valid @RequestBody StaticPageDTO staticPageDTO) {
        try {
            String res = adminService.addStaticPage(staticPageDTO);
            return new ResponseEntity<>(new CustomResponse(HttpStatus.OK, ResponseMessageConstant.CONTENT_UPDATED_SUCCESSFULLY, res), HttpStatus.OK);
        } catch (CustomException e) {
            log.error(e.getMessage(), e);
            return new ResponseEntity<>(new CustomResponse(e.getHttpStatus(), e.getMessage()), e.getHttpStatus());
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            return new ResponseEntity<>(new CustomResponse(HttpStatus.INTERNAL_SERVER_ERROR, ResponseMessageConstant.SOMETHING_WENT_WRONG), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping(value = APIEndpointConstant.ADMIN_URLS.GET_ALL_STATIC_PAGE)
    @ApiOperation(value = "Get All static page", response = Object.class, authorizations = {@Authorization(value = Constant.API_KEY)})
    public ResponseEntity<?> getAllStaticPage(@PageableDefault(size = 10, sort = "createdDate", direction = Sort.Direction.DESC) Pageable pageable) {
        try {
            PaginationResponse userResponseList = adminService.getAllStaticPage( pageable);
            return new ResponseEntity<>(
                    new CustomResponse(HttpStatus.OK, ResponseMessageConstant.CONTENT_FETCHED_SUCCESSFULLY, userResponseList), HttpStatus.OK);
        } catch (CustomException e) {
            log.error(e.getMessage(), e);
            return new ResponseEntity<>(new CustomResponse(e.getHttpStatus(), e.getMessage()), e.getHttpStatus());
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            return new ResponseEntity<>(new CustomResponse(HttpStatus.INTERNAL_SERVER_ERROR, ResponseMessageConstant.SOMETHING_WENT_WRONG), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @DeleteMapping(value = APIEndpointConstant.ADMIN_URLS.DELETE_STATIC_PAGE+"/{id}")
    @ApiOperation(value = "Get All static page", response = Object.class, authorizations = {@Authorization(value = Constant.API_KEY)})
    public ResponseEntity<?> deleteStaticPage(@PathVariable Long id) {
        try {
            adminService.deleteStaticPage( id);
            return new ResponseEntity<>(
                    new CustomResponse(HttpStatus.OK, ResponseMessageConstant.CONTENT_DELETED_SUCCESSFULLY, null), HttpStatus.OK);
        } catch (CustomException e) {
            log.error(e.getMessage(), e);
            return new ResponseEntity<>(new CustomResponse(e.getHttpStatus(), e.getMessage()), e.getHttpStatus());
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            return new ResponseEntity<>(new CustomResponse(HttpStatus.INTERNAL_SERVER_ERROR, ResponseMessageConstant.SOMETHING_WENT_WRONG), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    @GetMapping(value = APIEndpointConstant.ADMIN_URLS.DASHBOARD)
    @ApiOperation(value = "Dashboard", response = Object.class, authorizations = {@Authorization(value = Constant.API_KEY)})
    public ResponseEntity<?> dashboard() {
        try {
            Map<String,Long> res = adminService.dashboard();
            return new ResponseEntity<>(
                    new CustomResponse(HttpStatus.OK, ResponseMessageConstant.CONTENT_FETCHED_SUCCESSFULLY, res), HttpStatus.OK);
        } catch (CustomException e) {
            log.error(e.getMessage(), e);
            return new ResponseEntity<>(new CustomResponse(e.getHttpStatus(), e.getMessage()), e.getHttpStatus());
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            return new ResponseEntity<>(new CustomResponse(HttpStatus.INTERNAL_SERVER_ERROR, ResponseMessageConstant.SOMETHING_WENT_WRONG), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
