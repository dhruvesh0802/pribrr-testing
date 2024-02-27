package com.pb.controller;

import com.pb.constants.APIEndpointConstant;
import com.pb.constants.ResponseMessageConstant;
import com.pb.dto.PlanDTO;
import com.pb.exception.CustomException;
import com.pb.response.CustomResponse;
import com.pb.response.PaginationResponse;
import com.pb.service.PlanService;
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
@RequestMapping(value = APIEndpointConstant.ADMIN_URLS.BASE_URL + "/" + APIEndpointConstant.PLAN_MANAGEMENT_URLS.BASE_URL)
@Slf4j
@Api(value = APIEndpointConstant.ADMIN_URLS.BASE_URL, tags = "Plan Management")
@CrossOrigin
public class PlanController {
    
    @Autowired
    private PlanService planService;
    
    @PostMapping(value = APIEndpointConstant.PLAN_MANAGEMENT_URLS.ADD_PLAN)
    @ApiOperation(value = "Add New Plan", response = Object.class, authorizations = {@Authorization(value = Constant.API_KEY)})
    
    public ResponseEntity<CustomResponse> addPlan(@Valid @RequestBody PlanDTO planDTO) {
        try {
            PlanDTO savePlan = planService.addPlan(planDTO);
            return new ResponseEntity<>(new CustomResponse(HttpStatus.OK, ResponseMessageConstant.PLAN_ADDED_SUCCESSFULLY, savePlan), HttpStatus.OK);
        } catch (CustomException e) {
            log.error(e.getMessage(), e);
            return new ResponseEntity<>(new CustomResponse(e.getHttpStatus(), e.getMessage()), e.getHttpStatus());
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            return new ResponseEntity<>(new CustomResponse(HttpStatus.INTERNAL_SERVER_ERROR, ResponseMessageConstant.SOMETHING_WENT_WRONG), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    
    @GetMapping(value = APIEndpointConstant.PLAN_MANAGEMENT_URLS.GET_ALL_PLAN)
    @ApiOperation(value = "Get All Plans", response = Object.class, authorizations = {@Authorization(value = Constant.API_KEY)})
    
    public ResponseEntity<CustomResponse> getAllPlans(@PageableDefault(size = 10,
            sort = "createdDate",
            direction = Sort.Direction.DESC) Pageable pageable) {
        try {
            PaginationResponse plansResponse = planService.getAllPlans(pageable);
            return new ResponseEntity<>(new CustomResponse(HttpStatus.OK, ResponseMessageConstant.PLAN_FETCHED_SUCCESSFULLY, plansResponse), HttpStatus.OK);
        } catch (CustomException e) {
            log.error(e.getMessage(), e);
            return new ResponseEntity<>(new CustomResponse(e.getHttpStatus(), e.getMessage()), e.getHttpStatus());
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            return new ResponseEntity<>(new CustomResponse(HttpStatus.INTERNAL_SERVER_ERROR, ResponseMessageConstant.SOMETHING_WENT_WRONG), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    
    @PutMapping(value = APIEndpointConstant.PLAN_MANAGEMENT_URLS.UPDATE_PLAN)
    @ApiOperation(value = "Update Plan", response = Object.class, authorizations = {@Authorization(value = Constant.API_KEY)})
    
    public ResponseEntity<CustomResponse> updatePlan(@Valid @RequestBody PlanDTO planDTO) {
        try {
            PlanDTO planResponse = planService.updatePlan(planDTO);
            return new ResponseEntity<>(new CustomResponse(HttpStatus.OK, ResponseMessageConstant.PLAN_UPDATED_SUCCESSFULLY, planResponse), HttpStatus.OK);
        } catch (CustomException e) {
            log.error(e.getMessage(), e);
            return new ResponseEntity<>(new CustomResponse(e.getHttpStatus(), e.getMessage()), e.getHttpStatus());
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            return new ResponseEntity<>(new CustomResponse(HttpStatus.INTERNAL_SERVER_ERROR, ResponseMessageConstant.SOMETHING_WENT_WRONG), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    
    @DeleteMapping(value = APIEndpointConstant.PLAN_MANAGEMENT_URLS.DELETE_PLAN + "/{id}")
    @ApiOperation(value = "Delete Plan", response = Object.class, authorizations = {@Authorization(value = Constant.API_KEY)})
    public ResponseEntity<CustomResponse> deletePlan(@PathVariable Long id) {
        try {
            planService.deletePlan(id);
            return new ResponseEntity<>(new CustomResponse(HttpStatus.OK, ResponseMessageConstant.PLAN_DELETED_SUCCESSFULLY, null), HttpStatus.OK);
        } catch (CustomException e) {
            log.error(e.getMessage(), e);
            return new ResponseEntity<>(new CustomResponse(e.getHttpStatus(), e.getMessage()), e.getHttpStatus());
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            return new ResponseEntity<>(new CustomResponse(HttpStatus.INTERNAL_SERVER_ERROR, ResponseMessageConstant.SOMETHING_WENT_WRONG), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    
    @PatchMapping(value = APIEndpointConstant.PLAN_MANAGEMENT_URLS.CHANGE_PLAN_STATUS +"/{id}")
    @ApiOperation(value = "Change Plan Status",
            response = Object.class,
            authorizations = {@Authorization(value = Constant.API_KEY)})
    public ResponseEntity<CustomResponse> changePlanStatus(@PathVariable Long id) {
        try {
            planService.changePlanStatus(id);
            return new ResponseEntity<>(
                    new CustomResponse(HttpStatus.OK, ResponseMessageConstant.PLAN_STATUS_CHANGED_SUCCESSFULLY, null),
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
