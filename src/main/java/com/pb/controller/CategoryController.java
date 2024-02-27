package com.pb.controller;

import com.pb.constants.APIEndpointConstant;
import com.pb.constants.ResponseMessageConstant;
import com.pb.dto.CategoryDTO;
import com.pb.dto.CategoryFilteredDTO;
import com.pb.exception.CustomException;
import com.pb.response.CustomResponse;
import com.pb.response.PaginationResponse;
import com.pb.service.CategoryService;
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
@RequestMapping(value = APIEndpointConstant.ADMIN_URLS.BASE_URL + "/" + APIEndpointConstant.CATEGORY_URLS.BASE_URL)
@Slf4j
@Api(value = APIEndpointConstant.ADMIN_URLS.BASE_URL, tags = "Category")
@CrossOrigin
public class CategoryController {
    @Autowired
    private CategoryService categoryService;
    
    @PostMapping(value = APIEndpointConstant.CATEGORY_URLS.ADD_CATEGORY)
    @ApiOperation(value = "Create Category", response = Object.class, authorizations = {@Authorization(value = Constant.API_KEY)})
    
    public ResponseEntity<CustomResponse> addCategory(@Valid @RequestBody CategoryDTO categoryDTO) {
        try {
            CategoryDTO categoryResponse = categoryService.addCategory(categoryDTO);
            return new ResponseEntity<>(new CustomResponse(HttpStatus.OK, ResponseMessageConstant.CATEGORY_ADDED_SUCCESSFULLY, categoryResponse), HttpStatus.OK);
        } catch (CustomException e) {
            log.error(e.getMessage(), e);
            return new ResponseEntity<>(new CustomResponse(e.getHttpStatus(), e.getMessage()), e.getHttpStatus());
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            return new ResponseEntity<>(new CustomResponse(HttpStatus.INTERNAL_SERVER_ERROR, ResponseMessageConstant.SOMETHING_WENT_WRONG), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    
    @PutMapping(value = APIEndpointConstant.CATEGORY_URLS.UPDATE_CATEGORY)
    @ApiOperation(value = "Update Category", response = Object.class, authorizations = {@Authorization(value = Constant.API_KEY)})
    
    public ResponseEntity<CustomResponse> updateCategory(@Valid @RequestBody CategoryDTO categoryDTO) {
        try {
            CategoryDTO categoryResponse = categoryService.updateCategory(categoryDTO);
            return new ResponseEntity<>(new CustomResponse(HttpStatus.OK, ResponseMessageConstant.CATEGORY_UPDATED_SUCCESSFULLY, categoryResponse), HttpStatus.OK);
        } catch (CustomException e) {
            log.error(e.getMessage(), e);
            return new ResponseEntity<>(new CustomResponse(e.getHttpStatus(), e.getMessage()), e.getHttpStatus());
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            return new ResponseEntity<>(new CustomResponse(HttpStatus.INTERNAL_SERVER_ERROR, ResponseMessageConstant.SOMETHING_WENT_WRONG), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    
    @PostMapping(value = APIEndpointConstant.CATEGORY_URLS.GET_ALL_CATEGORY)
    @ApiOperation(value = "Get All Category", response = Object.class, authorizations = {@Authorization(value = Constant.API_KEY)})
    public ResponseEntity<CustomResponse> getAllCategory(@RequestBody CategoryFilteredDTO categoryFilteredDTO,
                                            @PageableDefault(size = 10,
                                                    sort = "createdDate",
                                                    direction = Sort.Direction.DESC) Pageable pageable) {
        try {
            PaginationResponse bannerResponse = categoryService.getAllCategory(categoryFilteredDTO,pageable);
            return new ResponseEntity<>(new CustomResponse(HttpStatus.OK, ResponseMessageConstant.CATEGORY_FETCH_SUCCESSFULLY, bannerResponse), HttpStatus.OK);
        } catch (CustomException e) {
            log.error(e.getMessage(), e);
            return new ResponseEntity<>(new CustomResponse(e.getHttpStatus(), e.getMessage()), e.getHttpStatus());
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            return new ResponseEntity<>(new CustomResponse(HttpStatus.INTERNAL_SERVER_ERROR, ResponseMessageConstant.SOMETHING_WENT_WRONG), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    
    @DeleteMapping(value = APIEndpointConstant.CATEGORY_URLS.DELETE_CATEGORY + "/{id}")
    @ApiOperation(value = "Delete Category", response = Object.class, authorizations = {@Authorization(value = Constant.API_KEY)})
    public ResponseEntity<CustomResponse> deleteCategory(@PathVariable Long id) {
        try {
            categoryService.deleteCategory(id);
            return new ResponseEntity<>(new CustomResponse(HttpStatus.OK, ResponseMessageConstant.CATEGORY_DELETED_SUCCESSFULLY, null), HttpStatus.OK);
        } catch (CustomException e) {
            log.error(e.getMessage(), e);
            return new ResponseEntity<>(new CustomResponse(e.getHttpStatus(), e.getMessage()), e.getHttpStatus());
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            return new ResponseEntity<>(new CustomResponse(HttpStatus.INTERNAL_SERVER_ERROR, ResponseMessageConstant.SOMETHING_WENT_WRONG), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    
    @PatchMapping(value = APIEndpointConstant.CATEGORY_URLS.CHANGE_CATEGORY_STATUS + "/{id}")
    @ApiOperation(value = "Change Category Status",
            response = Object.class,
            authorizations = {@Authorization(value = Constant.API_KEY)})
    public ResponseEntity<CustomResponse> changeCategoryStatus(@PathVariable Long id) {
        try {
            categoryService.changeCategoryStatus(id);
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

    @PostMapping(value = APIEndpointConstant.CATEGORY_URLS.GET_ALL_CATEGORY_DEPARTMENT_ID)
    @ApiOperation(value = "Get All Category By department", response = Object.class, authorizations = {@Authorization(value = Constant.API_KEY)})
    public ResponseEntity<CustomResponse> getAllCategoryByDepartment(@RequestBody CategoryFilteredDTO categoryFilteredDTO) {
        try {
            return new ResponseEntity<>(new CustomResponse(HttpStatus.OK, ResponseMessageConstant.CATEGORY_FETCH_SUCCESSFULLY, categoryService.getAllCategoryByDepartment(categoryFilteredDTO)), HttpStatus.OK);
        } catch (CustomException e) {
            log.error(e.getMessage(), e);
            return new ResponseEntity<>(new CustomResponse(e.getHttpStatus(), e.getMessage()), e.getHttpStatus());
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            return new ResponseEntity<>(new CustomResponse(HttpStatus.INTERNAL_SERVER_ERROR, ResponseMessageConstant.SOMETHING_WENT_WRONG), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
