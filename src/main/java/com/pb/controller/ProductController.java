package com.pb.controller;

import com.pb.constants.APIEndpointConstant;
import com.pb.constants.ResponseMessageConstant;
import com.pb.dto.ProductDTO;
import com.pb.exception.CustomException;
import com.pb.response.CustomResponse;
import com.pb.response.PaginationResponse;
import com.pb.service.ProductService;
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
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping(value = APIEndpointConstant.USER_URLS.BASE_URL + "/" + APIEndpointConstant.PRODUCT_URLS.BASE_URL)
@Slf4j
@Api(value = APIEndpointConstant.USER_URLS.BASE_URL, tags = "Product")
@CrossOrigin
public class ProductController {
    
    @Autowired
    private ProductService productService;
    
    @PostMapping(value = APIEndpointConstant.PRODUCT_URLS.ADD_PRODUCT)
    @ApiOperation(value = "Create Product", response = Object.class, authorizations = {@Authorization(value = Constant.API_KEY)})
    
    public ResponseEntity<CustomResponse> addProduct(@Valid @ModelAttribute ProductDTO productDTO,
                                       @RequestPart(value = "product_image",required = false) List<MultipartFile> productMedia) {
        try {
             productDTO = productService.addProduct(productDTO, productMedia);
            return new ResponseEntity<>(new CustomResponse(HttpStatus.OK, ResponseMessageConstant.PRODUCT_ADDED_SUCCESSFULLY, productDTO), HttpStatus.OK);
        } catch (CustomException e) {
            log.error(e.getMessage(), e);
            return new ResponseEntity<>(new CustomResponse(e.getHttpStatus(), e.getMessage()), e.getHttpStatus());
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            return new ResponseEntity<>(new CustomResponse(HttpStatus.INTERNAL_SERVER_ERROR, ResponseMessageConstant.SOMETHING_WENT_WRONG), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping(value = APIEndpointConstant.PRODUCT_URLS.GET_ALL_PRODUCT_OF_USER)
    @ApiOperation(value = "Get All Product", response = Object.class, authorizations = {@Authorization(value = Constant.API_KEY)})
    public ResponseEntity<CustomResponse> getAllProductOfUser(@PageableDefault(size = 10,
            sort = "createdDate",
            direction = Sort.Direction.DESC) Pageable pageable,Long userId) {
        try {
            PaginationResponse bannerResponse = productService.getAllProduct(pageable,userId);
            return new ResponseEntity<>(new CustomResponse(HttpStatus.OK, ResponseMessageConstant.PRODUCT_FETCH_SUCCESSFULLY, bannerResponse), HttpStatus.OK);
        } catch (CustomException e) {
            log.error(e.getMessage(), e);
            return new ResponseEntity<>(new CustomResponse(e.getHttpStatus(), e.getMessage()), e.getHttpStatus());
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            return new ResponseEntity<>(new CustomResponse(HttpStatus.INTERNAL_SERVER_ERROR, ResponseMessageConstant.SOMETHING_WENT_WRONG), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @DeleteMapping(value = APIEndpointConstant.PRODUCT_URLS.DELETE_PRODUCT + "/{id}")
    @ApiOperation(value = "Delete Product", response = Object.class, authorizations = {@Authorization(value = Constant.API_KEY)})
    public ResponseEntity<CustomResponse> deleteProduct(@PathVariable Long id) {
        try {
            productService.deleteProduct(id);
            return new ResponseEntity<>(new CustomResponse(HttpStatus.OK, ResponseMessageConstant.PRODUCT_DELETED_SUCCESSFULLY, null), HttpStatus.OK);
        } catch (CustomException e) {
            log.error(e.getMessage(), e);
            return new ResponseEntity<>(new CustomResponse(e.getHttpStatus(), e.getMessage()), e.getHttpStatus());
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            return new ResponseEntity<>(new CustomResponse(HttpStatus.INTERNAL_SERVER_ERROR, ResponseMessageConstant.SOMETHING_WENT_WRONG), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
