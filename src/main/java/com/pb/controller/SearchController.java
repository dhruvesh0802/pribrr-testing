package com.pb.controller;

import com.pb.constants.APIEndpointConstant;
import com.pb.constants.ResponseMessageConstant;
import com.pb.dto.ParameterDTO;
import com.pb.dto.search.SearchPaginationResDTO;
import com.pb.exception.CustomException;
import com.pb.response.CustomResponse;
import com.pb.service.SearchService;
import com.pb.utils.Constant;
import io.swagger.annotations.Api;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

@RestController
@RequestMapping(value = APIEndpointConstant.USER_URLS.BASE_URL+APIEndpointConstant.SEARCH_URLS.BASE_URL)
@Slf4j
@Api(value = APIEndpointConstant.USER_URLS.BASE_URL+APIEndpointConstant.SEARCH_URLS.BASE_URL,
        tags = "Search")
@CrossOrigin
public class SearchController {

    @Autowired
    private SearchService searchService;

    @PostMapping
    public ResponseEntity<?> getAllAdmin(HttpServletRequest req, @RequestBody ParameterDTO parameterDTO) {
        try {

            SearchPaginationResDTO search = searchService.searchOnDb(req.getHeader(Constant.ACCESS_TOKEN),parameterDTO);
            return new ResponseEntity<>(
                    new CustomResponse(HttpStatus.OK, ResponseMessageConstant.USERS_FETCHED_SUCCESSFULLY, search), HttpStatus.OK);
        } catch (CustomException e) {
            log.error(e.getMessage(), e);
            return new ResponseEntity<>(new CustomResponse(e.getHttpStatus(), e.getMessage()), e.getHttpStatus());
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            return new ResponseEntity<>(new CustomResponse(HttpStatus.INTERNAL_SERVER_ERROR, ResponseMessageConstant.SOMETHING_WENT_WRONG), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
