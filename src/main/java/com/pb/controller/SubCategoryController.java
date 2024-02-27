package com.pb.controller;

import com.pb.constants.APIEndpointConstant;
import com.pb.constants.ResponseMessageConstant;
import com.pb.dto.CategoryFilteredDTO;
import com.pb.dto.SubCategoryDTO;
import com.pb.exception.CustomException;
import com.pb.response.CustomResponse;
import com.pb.response.PaginationResponse;
import com.pb.service.SubCategoryService;
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

@RestController
@RequestMapping(value = APIEndpointConstant.ADMIN_URLS.BASE_URL + "/" + APIEndpointConstant.SUB_CATEGORY_URLS.BASE_URL)
@Slf4j
@Api(value = APIEndpointConstant.ADMIN_URLS.BASE_URL, tags = "Sub Category")
@CrossOrigin
public class SubCategoryController {
    @Autowired
    private SubCategoryService subCategoryService;
    
    @PostMapping(value = APIEndpointConstant.SUB_CATEGORY_URLS.ADD_SUB_CATEGORY)
    @ApiOperation(value = "Create Category", response = Object.class, authorizations = {@Authorization(value = Constant.API_KEY)})
    
    public ResponseEntity<?> addSubCategory(@Valid @RequestBody SubCategoryDTO subCategoryDTO) {
        try {
            SubCategoryDTO subCategoryResponse = subCategoryService.addSubCategory(subCategoryDTO);
            return new ResponseEntity<>(new CustomResponse(HttpStatus.OK, ResponseMessageConstant.SUB_CATEGORY_ADDED_SUCCESSFULLY, subCategoryResponse), HttpStatus.OK);
        } catch (CustomException e) {
            log.error(e.getMessage(), e);
            return new ResponseEntity<>(new CustomResponse(e.getHttpStatus(), e.getMessage()), e.getHttpStatus());
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            return new ResponseEntity<>(new CustomResponse(HttpStatus.INTERNAL_SERVER_ERROR, ResponseMessageConstant.SOMETHING_WENT_WRONG), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    
    @PutMapping(value = APIEndpointConstant.SUB_CATEGORY_URLS.UPDATE_SUB_CATEGORY)
    @ApiOperation(value = "Update Category", response = Object.class, authorizations = {@Authorization(value = Constant.API_KEY)})
    
    public ResponseEntity<?> updateCategory(@Valid @RequestBody SubCategoryDTO subCategoryDTO) {
        try {
            SubCategoryDTO subCategoryResponse = subCategoryService.updateSubCategory(subCategoryDTO);
            return new ResponseEntity<>(new CustomResponse(HttpStatus.OK, ResponseMessageConstant.SUB_CATEGORY_UPDATED_SUCCESSFULLY, subCategoryResponse), HttpStatus.OK);
        } catch (CustomException e) {
            log.error(e.getMessage(), e);
            return new ResponseEntity<>(new CustomResponse(e.getHttpStatus(), e.getMessage()), e.getHttpStatus());
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            return new ResponseEntity<>(new CustomResponse(HttpStatus.INTERNAL_SERVER_ERROR, ResponseMessageConstant.SOMETHING_WENT_WRONG), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    
    @PostMapping(value = APIEndpointConstant.SUB_CATEGORY_URLS.GET_ALL_SUB_CATEGORY)
    @ApiOperation(value = "Get All Category", response = Object.class, authorizations = {@Authorization(value = Constant.API_KEY)})
    
    public ResponseEntity<?> getAllCategory(@RequestBody CategoryFilteredDTO categoryFilteredDTO,
                                            @PageableDefault(size = 10,
                                                    sort = "createdDate",
                                                    direction = Sort.Direction.DESC) Pageable pageable) {
        try {
            PaginationResponse bannerResponse = subCategoryService.getAllSubCategory(categoryFilteredDTO,pageable);
            return new ResponseEntity<>(new CustomResponse(HttpStatus.OK, ResponseMessageConstant.CATEGORY_FETCH_SUCCESSFULLY, bannerResponse), HttpStatus.OK);
        } catch (CustomException e) {
            log.error(e.getMessage(), e);
            return new ResponseEntity<>(new CustomResponse(e.getHttpStatus(), e.getMessage()), e.getHttpStatus());
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            return new ResponseEntity<>(new CustomResponse(HttpStatus.INTERNAL_SERVER_ERROR, ResponseMessageConstant.SOMETHING_WENT_WRONG), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    
    @DeleteMapping(value = APIEndpointConstant.SUB_CATEGORY_URLS.DELETE_SUB_CATEGORY + "/{id}")
    @ApiOperation(value = "Delete Category", response = Object.class, authorizations = {@Authorization(value = Constant.API_KEY)})
    public ResponseEntity<?> deleteCategory(@PathVariable Long id) {
        try {
            subCategoryService.deleteSubCategory(id);
            return new ResponseEntity<>(new CustomResponse(HttpStatus.OK, ResponseMessageConstant.CATEGORY_DELETED_SUCCESSFULLY, null), HttpStatus.OK);
        } catch (CustomException e) {
            log.error(e.getMessage(), e);
            return new ResponseEntity<>(new CustomResponse(e.getHttpStatus(), e.getMessage()), e.getHttpStatus());
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            return new ResponseEntity<>(new CustomResponse(HttpStatus.INTERNAL_SERVER_ERROR, ResponseMessageConstant.SOMETHING_WENT_WRONG), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    
    @PatchMapping(value = APIEndpointConstant.SUB_CATEGORY_URLS.CHANGE_SUB_CATEGORY_STATUS + "/{id}")
    @ApiOperation(value = "Change Category Status",
            response = Object.class,
            authorizations = {@Authorization(value = Constant.API_KEY)})
    public ResponseEntity<?> changeCategoryStatus(@PathVariable Long id) {
        try {
            subCategoryService.changeSubCategoryStatus(id);
            return new ResponseEntity<>(
                    new CustomResponse(HttpStatus.OK, ResponseMessageConstant.CATEGORY_STATUS_CHANGED_SUCCESSFULLY, null),
                    HttpStatus.OK);
        } catch (CustomException e) {
            log.error(e.getMessage(), e);
            return new ResponseEntity<>(new CustomResponse(e.getHttpStatus(), e.getMessage()), e.getHttpStatus());
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            return new ResponseEntity<>(new CustomResponse(HttpStatus.INTERNAL_SERVER_ERROR, ResponseMessageConstant.SOMETHING_WENT_WRONG), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
