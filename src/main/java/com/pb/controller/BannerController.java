package com.pb.controller;

import com.pb.constants.APIEndpointConstant;
import com.pb.constants.ResponseMessageConstant;
import com.pb.dto.BannerDTO;
import com.pb.exception.CustomException;
import com.pb.response.CustomResponse;
import com.pb.response.PaginationResponse;
import com.pb.service.BannerService;
import com.pb.utils.Constant;
import com.pb.validation.FileSizeValidation;
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
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;

@RestController
@RequestMapping(value = APIEndpointConstant.ADMIN_URLS.BASE_URL + "/" + APIEndpointConstant.BANNER_URLS.BASE_URL)
@Slf4j
@Api(value = APIEndpointConstant.ADMIN_URLS.BASE_URL, tags = "Banner")
@CrossOrigin
public class BannerController {
    
    @Autowired
    private BannerService bannerService;
    
    @PostMapping(value = APIEndpointConstant.BANNER_URLS.ADD_BANNER)
    @ApiOperation(value = "Create Banner", response = Object.class, authorizations = {@Authorization(value = Constant.API_KEY)})
    
    public ResponseEntity<CustomResponse> addBanner(@Valid @ModelAttribute BannerDTO bannerDTO,
                                       @RequestPart("banner_image") @FileSizeValidation MultipartFile bannerImage) {
        try {
            BannerDTO bannerResponse = bannerService.addBanner(bannerDTO, bannerImage);
            return new ResponseEntity<>(new CustomResponse(HttpStatus.OK, ResponseMessageConstant.BANNER_ADDED_SUCCESSFULLY, bannerResponse), HttpStatus.OK);
        } catch (CustomException e) {
            log.error(e.getMessage(), e);
            return new ResponseEntity<>(new CustomResponse(e.getHttpStatus(), e.getMessage()), e.getHttpStatus());
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            return new ResponseEntity<>(new CustomResponse(HttpStatus.INTERNAL_SERVER_ERROR, ResponseMessageConstant.SOMETHING_WENT_WRONG), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    
    @GetMapping(value = APIEndpointConstant.BANNER_URLS.GET_ALL_BANNER)
    @ApiOperation(value = "Get All Banner", response = Object.class, authorizations = {@Authorization(value = Constant.API_KEY)})
    
    public ResponseEntity<CustomResponse> getAllBanner(@PageableDefault(size = 10,
            sort = "createdDate",
            direction = Sort.Direction.DESC) Pageable pageable) {
        try {
            PaginationResponse bannerResponse = bannerService.getAllBanner(pageable);
            return new ResponseEntity<>(new CustomResponse(HttpStatus.OK, ResponseMessageConstant.BANNER_FETCH_SUCCESSFULLY, bannerResponse), HttpStatus.OK);
        } catch (CustomException e) {
            log.error(e.getMessage(), e);
            return new ResponseEntity<>(new CustomResponse(e.getHttpStatus(), e.getMessage()), e.getHttpStatus());
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            return new ResponseEntity<>(new CustomResponse(HttpStatus.INTERNAL_SERVER_ERROR, ResponseMessageConstant.SOMETHING_WENT_WRONG), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    
    @PutMapping(value = APIEndpointConstant.BANNER_URLS.UPDATE_BANNER)
    @ApiOperation(value = "Update Banner", response = Object.class, authorizations = {@Authorization(value = Constant.API_KEY)})
    
    public ResponseEntity<CustomResponse> updateBanner(@Valid @ModelAttribute BannerDTO bannerDTO, @RequestPart(value = "banner_image", required = false) @FileSizeValidation MultipartFile bannerImage) {
        try {
            BannerDTO bannerResponse = bannerService.updateBanner(bannerDTO, bannerImage);
            return new ResponseEntity<>(new CustomResponse(HttpStatus.OK, ResponseMessageConstant.BANNER_UPDATED_SUCCESSFULLY, bannerResponse), HttpStatus.OK);
        } catch (CustomException e) {
            log.error(e.getMessage(), e);
            return new ResponseEntity<>(new CustomResponse(e.getHttpStatus(), e.getMessage()), e.getHttpStatus());
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            return new ResponseEntity<>(new CustomResponse(HttpStatus.INTERNAL_SERVER_ERROR, ResponseMessageConstant.SOMETHING_WENT_WRONG), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    
    @DeleteMapping(value = APIEndpointConstant.BANNER_URLS.DELETE_BANNER + "/{id}")
    @ApiOperation(value = "Delete Banner", response = Object.class, authorizations = {@Authorization(value = Constant.API_KEY)})
    public ResponseEntity<CustomResponse> deleteBanner(@PathVariable Long id) {
        try {
            bannerService.deleteBanner(id);
            return new ResponseEntity<>(new CustomResponse(HttpStatus.OK, ResponseMessageConstant.BANNER_DELETED_SUCCESSFULLY, null), HttpStatus.OK);
        } catch (CustomException e) {
            log.error(e.getMessage(), e);
            return new ResponseEntity<>(new CustomResponse(e.getHttpStatus(), e.getMessage()), e.getHttpStatus());
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            return new ResponseEntity<>(new CustomResponse(HttpStatus.INTERNAL_SERVER_ERROR, ResponseMessageConstant.SOMETHING_WENT_WRONG), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping(value = APIEndpointConstant.BANNER_URLS.GET_ALL_BANNER_BY_LOCATION)
    @ApiOperation(value = "Get All Banner By Location", response = Object.class, authorizations = {@Authorization(value = Constant.API_KEY)})

    public ResponseEntity<CustomResponse> getAllBannerByLocation(@PageableDefault(size = 10,
            sort = "createdDate",
            direction = Sort.Direction.DESC) Pageable pageable,@RequestParam(value="location") String location) {
        try {
            PaginationResponse bannerResponse = bannerService.getAllBannerByLocation(pageable,location);
            return new ResponseEntity<>(new CustomResponse(HttpStatus.OK, ResponseMessageConstant.BANNER_FETCH_SUCCESSFULLY, bannerResponse), HttpStatus.OK);
        } catch (CustomException e) {
            log.error(e.getMessage(), e);
            return new ResponseEntity<>(new CustomResponse(e.getHttpStatus(), e.getMessage()), e.getHttpStatus());
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            return new ResponseEntity<>(new CustomResponse(HttpStatus.INTERNAL_SERVER_ERROR, ResponseMessageConstant.SOMETHING_WENT_WRONG), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
