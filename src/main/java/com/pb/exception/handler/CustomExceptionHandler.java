package com.pb.exception.handler;


import com.pb.constants.ResponseMessageConstant;
import com.pb.constants.ValidationMessageConstant;
import com.pb.dto.ErrorDTO;
import com.pb.exception.CustomException;
import org.apache.tomcat.util.http.fileupload.impl.FileSizeLimitExceededException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.validation.BindException;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import javax.servlet.http.HttpServletRequest;

@RestControllerAdvice
public class CustomExceptionHandler {
    @ExceptionHandler({CustomException.class, AccessDeniedException.class})
    public ResponseEntity<ErrorDTO> handleCustomException(CustomException e, HttpServletRequest request) {
        ErrorDTO errorDTO = new ErrorDTO(e.getHttpStatus().value(), e.getMessage());
        return ResponseEntity.status(e.getHttpStatus()).body(errorDTO);
    }

    @ExceptionHandler({MethodArgumentNotValidException.class})
    public ResponseEntity<ErrorDTO> handleArgumentException(MethodArgumentNotValidException e, HttpServletRequest request) {
        BindingResult bindingResult = e.getBindingResult();
        String errorMessage = bindingResult.getAllErrors().get(0).getDefaultMessage();
        ErrorDTO errorDTO = new ErrorDTO(HttpStatus.BAD_REQUEST.value(), errorMessage);
        return ResponseEntity.status(HttpStatus.BAD_REQUEST.value()).body(errorDTO);
    }
    
    @ExceptionHandler(BindException.class)
    public ResponseEntity<ErrorDTO> handleBindException(BindException e, HttpServletRequest request) {
        BindingResult bindingResult = e.getBindingResult();
        String errorMessage = bindingResult.getAllErrors().get(0).getDefaultMessage();
        ErrorDTO errorDTO = new ErrorDTO(HttpStatus.BAD_REQUEST.value(), errorMessage);
        return ResponseEntity.status(HttpStatus.BAD_REQUEST.value()).body(errorDTO);
    }
    
    @ExceptionHandler(value = AuthenticationException.class)
    public ResponseEntity<ErrorDTO> handleAuthenticationException(HttpServletRequest request, AuthenticationException e) {
        ErrorDTO errorDTO = new ErrorDTO(HttpStatus.UNAUTHORIZED.value(), ResponseMessageConstant.INVALID_JWT_TOKEN);
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(errorDTO);
    }
    
    @ExceptionHandler(FileSizeLimitExceededException.class)
    public ResponseEntity<ErrorDTO> handleBindException(FileSizeLimitExceededException e, HttpServletRequest request) {
        ErrorDTO errorDTO = new ErrorDTO(HttpStatus.BAD_REQUEST.value(), ValidationMessageConstant.MAX_FILE_SIZE_EXCEED);
        return ResponseEntity.status(HttpStatus.BAD_REQUEST.value()).body(errorDTO);
    }
}
