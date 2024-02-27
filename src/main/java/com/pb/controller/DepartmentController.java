package com.pb.controller;

import com.pb.constants.APIEndpointConstant;
import com.pb.constants.ResponseMessageConstant;
import com.pb.dto.DepartmentDTO;
import com.pb.dto.DepartmentFilteredDTO;
import com.pb.exception.CustomException;
import com.pb.model.CategoryEntity;
import com.pb.response.CustomResponse;
import com.pb.response.PaginationResponse;
import com.pb.service.DepartmentService;
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
import java.util.List;
import java.util.Objects;

@RestController
@RequestMapping(value = APIEndpointConstant.ADMIN_URLS.BASE_URL + "/" + APIEndpointConstant.DEPARTMENT_URLS.BASE_URL)
@Slf4j
@Api(value = APIEndpointConstant.ADMIN_URLS.BASE_URL, tags = "Department")
@CrossOrigin
public class DepartmentController {
    @Autowired
    private DepartmentService departmentService;
    
    @PostMapping(value = APIEndpointConstant.DEPARTMENT_URLS.ADD_DEPARTMENT)
    @ApiOperation(value = "Create Department", response = Object.class, authorizations = {@Authorization(value = Constant.API_KEY)})
    
    public ResponseEntity<CustomResponse> addDepartment(@Valid @RequestBody DepartmentDTO departmentDTO) {
        try {
            DepartmentDTO departmentResponse = departmentService.addDepartment(departmentDTO);
            return new ResponseEntity<>(new CustomResponse(HttpStatus.OK, ResponseMessageConstant.DEPARTMENT_ADDED_SUCCESSFULLY, departmentResponse), HttpStatus.OK);
        } catch (CustomException e) {
            log.error(e.getMessage(), e);
            return new ResponseEntity<>(new CustomResponse(e.getHttpStatus(), e.getMessage()), e.getHttpStatus());
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            return new ResponseEntity<>(new CustomResponse(HttpStatus.INTERNAL_SERVER_ERROR, ResponseMessageConstant.SOMETHING_WENT_WRONG), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    
    @PutMapping(value = APIEndpointConstant.DEPARTMENT_URLS.UPDATE_DEPARTMENT)
    @ApiOperation(value = "Update Department", response = Object.class, authorizations = {@Authorization(value = Constant.API_KEY)})
    
    public ResponseEntity<CustomResponse> updateDepartment(@Valid @RequestBody DepartmentDTO departmentDTO) {
        try {
            DepartmentDTO departmentResponse = departmentService.updateDepartment(departmentDTO);
            return new ResponseEntity<>(new CustomResponse(HttpStatus.OK, ResponseMessageConstant.DEPARTMENT_UPDATED_SUCCESSFULLY, departmentResponse), HttpStatus.OK);
        } catch (CustomException e) {
            log.error(e.getMessage(), e);
            return new ResponseEntity<>(new CustomResponse(e.getHttpStatus(), e.getMessage()), e.getHttpStatus());
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            return new ResponseEntity<>(new CustomResponse(HttpStatus.INTERNAL_SERVER_ERROR, ResponseMessageConstant.SOMETHING_WENT_WRONG), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    
    @PostMapping(value = APIEndpointConstant.DEPARTMENT_URLS.GET_ALL_DEPARTMENT)
    @ApiOperation(value = "Get All Department", response = Object.class, authorizations = {@Authorization(value = Constant.API_KEY)})
    
    public ResponseEntity<CustomResponse> getAllDepartment(@RequestBody DepartmentFilteredDTO departmentFilteredDTO,
                                              @PageableDefault(size = 10,
                                                      sort = "createdDate",
                                                      direction = Sort.Direction.DESC) Pageable pageable) {
        try {
            PaginationResponse bannerResponse = departmentService.getAllDepartment(departmentFilteredDTO,pageable);
            return new ResponseEntity<>(new CustomResponse(HttpStatus.OK, ResponseMessageConstant.DEPARTMENT_FETCH_SUCCESSFULLY, bannerResponse), HttpStatus.OK);
        } catch (CustomException e) {
            log.error(e.getMessage(), e);
            return new ResponseEntity<>(new CustomResponse(e.getHttpStatus(), e.getMessage()), e.getHttpStatus());
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            return new ResponseEntity<>(new CustomResponse(HttpStatus.INTERNAL_SERVER_ERROR, ResponseMessageConstant.SOMETHING_WENT_WRONG), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    
    @DeleteMapping(value = APIEndpointConstant.DEPARTMENT_URLS.DELETE_DEPARTMENT + "/{id}")
    @ApiOperation(value = "Delete Department", response = Object.class, authorizations = {@Authorization(value = Constant.API_KEY)})
    public ResponseEntity<CustomResponse> deleteDepartment(@PathVariable Long id) {
        try {
            List<CategoryEntity> categoryEntities =  departmentService.deleteDepartment(id);
            if(Objects.nonNull(categoryEntities)) {
                return new ResponseEntity<>(
                        new CustomResponse(HttpStatus.OK, ResponseMessageConstant.DEPARTMENT_CANT_BE_DELETED, null),
                        HttpStatus.OK);
            }
            else {
                return new ResponseEntity<>(
                        new CustomResponse(HttpStatus.OK, ResponseMessageConstant.DEPARTMENT_DELETED_SUCCESSFULLY, null),
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
    
    @PatchMapping(value = APIEndpointConstant.DEPARTMENT_URLS.CHANGE_DEPARTMENT_STATUS + "/{id}")
    @ApiOperation(value = "Change Department Status",
            response = Object.class,
            authorizations = {@Authorization(value = Constant.API_KEY)})
    public ResponseEntity<CustomResponse> changeDepartmentStatus(@PathVariable Long id) {
        try {
            List<CategoryEntity> categoryEntities = departmentService.changeDepartmentStatus(id);
            if (Objects.nonNull(categoryEntities)) {
                return new ResponseEntity<>(
                        new CustomResponse(HttpStatus.OK, ResponseMessageConstant.DEPARTMENT_STATUS_CANT_BE_CHANGED, null),
                        HttpStatus.OK);
            }
            else {
                return new ResponseEntity<>(
                        new CustomResponse(HttpStatus.OK, ResponseMessageConstant.DEPARTMENT_STATUS_CHANGED_SUCCESSFULLY, null),
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

    @GetMapping(value = APIEndpointConstant.DEPARTMENT_URLS.GET_DEPARTMENT_BY_ID)
    @ApiOperation(value = "Get Department By Id", response = Object.class, authorizations = {@Authorization(value = Constant.API_KEY)})
    public ResponseEntity<CustomResponse> getAllProductOfUser(Long departmentId) {
        try {
            DepartmentDTO departmentDTO = departmentService.getDepartmentById(departmentId);
            return new ResponseEntity<>(new CustomResponse(HttpStatus.OK, ResponseMessageConstant.DEPARTMENT_FETCH_SUCCESSFULLY, departmentDTO), HttpStatus.OK);
        } catch (CustomException e) {
            log.error(e.getMessage(), e);
            return new ResponseEntity<>(new CustomResponse(e.getHttpStatus(), e.getMessage()), e.getHttpStatus());
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            return new ResponseEntity<>(new CustomResponse(HttpStatus.INTERNAL_SERVER_ERROR, ResponseMessageConstant.SOMETHING_WENT_WRONG), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
