package com.pb.exception;

import org.springframework.http.HttpStatus;

public class CustomException extends RuntimeException {
    
    private final String message;
    private final HttpStatus httpStatus;
    
    public CustomException(HttpStatus httpStatus, String message) {
        this.httpStatus = httpStatus;
        this.message = message;
    }
    
    @Override
    public String getMessage() {
        return message;
    }
    
    public HttpStatus getHttpStatus() {
        return httpStatus;
    }
}
