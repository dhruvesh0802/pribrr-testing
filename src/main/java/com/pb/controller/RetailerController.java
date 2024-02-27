package com.pb.controller;


import com.pb.constants.APIEndpointConstant;
import com.pb.constants.ResponseMessageConstant;
import com.pb.dto.UserDTO;
import com.pb.dto.UserFilterDTO;
import com.pb.dto.profile.*;
import com.pb.exception.CustomException;
import com.pb.response.CustomResponse;
import com.pb.response.PaginationResponse;
import com.pb.service.AdminService;
import com.pb.service.ProfileService;
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
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.Objects;

@RestController
@RequestMapping(value = APIEndpointConstant.ADMIN_URLS.BASE_URL+APIEndpointConstant.RETAILER_URLS.BASE_URL)
@Slf4j
@Api(value = APIEndpointConstant.ADMIN_URLS.BASE_URL+APIEndpointConstant.RETAILER_URLS.BASE_URL,
        tags = "Retailer")
@CrossOrigin
public class RetailerController {
    @Autowired
    private AdminService adminService;

    @Autowired
    ProfileService profileService;

    @PostMapping(value = APIEndpointConstant.COMMON_URL.LOGIN)
    public ResponseEntity<?> login(@Valid @RequestBody UserDTO userDTO) {
        try {
            userDTO.setRole("USER");
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
    @PostMapping(value = APIEndpointConstant.COMMON_URL.GET_ALL_USER)
    public ResponseEntity<CustomResponse> getAllAdmin(@RequestBody UserFilterDTO userFilterDTO, @PageableDefault(size = 10, sort = "created_date", direction = Sort.Direction.DESC) Pageable pageable) {
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

    @PostMapping(value = APIEndpointConstant.COMMON_URL.CHANGE_STATUS)
    public ResponseEntity<CustomResponse> changeUserStatus(@RequestParam(value = "id") Long id) {
        try {
            boolean b = adminService.changeStatusOfUser(id);
            if (b){
                return new ResponseEntity<>(
                        new CustomResponse(HttpStatus.OK, ResponseMessageConstant.USER_STATUS_CHANGE_SUCCESSFULLY, true), HttpStatus.OK);
            }
            else {
                return new ResponseEntity<>(
                        new CustomResponse(HttpStatus.OK, ResponseMessageConstant.USER_STATUS_CANT_BE_CHANGED, false), HttpStatus.OK);
            }

        } catch (CustomException e) {
            log.error(e.getMessage(), e);
            return new ResponseEntity<>(new CustomResponse(e.getHttpStatus(), e.getMessage()), e.getHttpStatus());
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            return new ResponseEntity<>(new CustomResponse(HttpStatus.INTERNAL_SERVER_ERROR, ResponseMessageConstant.SOMETHING_WENT_WRONG), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping(value = APIEndpointConstant.RETAILER_URLS.DELETE_RETAILER)
    public ResponseEntity<CustomResponse> deleteRetailer(@RequestParam(value = "id") Long id) {
        try {
            Long userId = adminService.deleteUser(id);
            return new ResponseEntity<>(
                    new CustomResponse(HttpStatus.OK,ResponseMessageConstant.RETAILER_DELETED_SUCCESSFULLY,userId),
                    HttpStatus.OK);
        } catch (CustomException e) {
            log.error(e.getMessage(), e);
            return new ResponseEntity<>(new CustomResponse(e.getHttpStatus(),e.getMessage()),e.getHttpStatus());
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            return new ResponseEntity<>(new CustomResponse(HttpStatus.INTERNAL_SERVER_ERROR,ResponseMessageConstant.SOMETHING_WENT_WRONG),HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
