package com.pb.controller;

import com.pb.constants.APIEndpointConstant;
import com.pb.constants.ResponseMessageConstant;
import com.pb.dto.CountryDTO;
import com.pb.dto.TimezoneDTO;
import com.pb.dto.UserFilterDTO;
import com.pb.exception.CustomException;
import com.pb.response.CustomResponse;
import com.pb.response.PaginationResponse;
import com.pb.service.CountryService;
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

import java.util.List;

@RestController
@RequestMapping(value = APIEndpointConstant.COUNTRY_URL.BASE_URL)
@Slf4j
@Api(value = APIEndpointConstant.COUNTRY_URL.BASE_URL,
        tags = "Country")
@CrossOrigin
public class CountryController {
    
    @Autowired
    private CountryService countryService;

    @GetMapping(value = APIEndpointConstant.COUNTRY_URL.GET_ALL_COUNTRY)
    @ApiOperation(value = "Get Country",
            response = Object.class,
            authorizations = {@Authorization(value = Constant.API_KEY)})
    public ResponseEntity<CustomResponse> getAllCountry() {
        try {
            List<CountryDTO> countryList = countryService.getAllCountry();
            return new ResponseEntity<>(
                    new CustomResponse(HttpStatus.OK, ResponseMessageConstant.COUNTRY_FETCHED_SUCCESSFULLY,countryList),
                    HttpStatus.OK);
        } catch (CustomException e) {
            log.error(e.getMessage(), e);
            return new ResponseEntity<>(new CustomResponse(e.getHttpStatus(),e.getMessage()),e.getHttpStatus());
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            return new ResponseEntity<>(new CustomResponse(HttpStatus.INTERNAL_SERVER_ERROR,ResponseMessageConstant.SOMETHING_WENT_WRONG),HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    @ApiOperation(value = "Clear Country",
            response = Object.class,
            authorizations = {@Authorization(value = Constant.API_KEY)})
    @GetMapping(value = APIEndpointConstant.COUNTRY_URL.CLEAR_COUNTRY_CACHE)
    public ResponseEntity<CustomResponse> clearCountyCache() {
        try {
            countryService.clearcache();
            return new ResponseEntity<>(
                    new CustomResponse(HttpStatus.OK, "Country cache cleared......!"),
                    HttpStatus.OK);
        } catch (CustomException e) {
            log.error(e.getMessage(), e);
            return new ResponseEntity<>(new CustomResponse(e.getHttpStatus(),e.getMessage()),e.getHttpStatus());
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            return new ResponseEntity<>(new CustomResponse(HttpStatus.INTERNAL_SERVER_ERROR,ResponseMessageConstant.SOMETHING_WENT_WRONG),HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping(value = APIEndpointConstant.COUNTRY_URL.GET_ALL_TIMEZONE)
    @ApiOperation(value = "Get TimeZone",
            response = Object.class,
            authorizations = {@Authorization(value = Constant.API_KEY)})
    public ResponseEntity<CustomResponse> getAllTimezone() {
        try {
            List<TimezoneDTO> countryList = countryService.getAllTimeZone();
            return new ResponseEntity<>(
                    new CustomResponse(HttpStatus.OK, ResponseMessageConstant.TIMEZONE_FETCHED_SUCCESSFULLY,countryList),
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
