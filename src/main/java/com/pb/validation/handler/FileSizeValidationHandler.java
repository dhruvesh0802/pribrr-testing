package com.pb.validation.handler;

import com.pb.validation.FileSizeValidation;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class FileSizeValidationHandler implements ConstraintValidator<FileSizeValidation, MultipartFile> {
    
    private static final Integer MB=1024;
    
    @Override
    public boolean isValid(MultipartFile multipartFile, ConstraintValidatorContext context) {
    
        if(multipartFile==null)
            return true;
    
        return multipartFile.getSize()< MB;
    }
}
