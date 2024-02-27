package com.pb.validation.handler;

import com.pb.validation.ValidPassword;
import org.apache.commons.lang3.StringUtils;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ValidPasswordHandler implements ConstraintValidator<ValidPassword, String> {
   
    private static final String PASSWORD_REGEX = "^(?=.*[0-9])"
            + "(?=.*[a-z])(?=.*[A-Z])"
            + "(?=.*[@#$%^&+=])"
            + "(?=\\S+$).{8,20}$";
    
    @Override
    public boolean isValid(String password, ConstraintValidatorContext context) {
        if(StringUtils.isNotBlank(password)){
            Pattern pattern = Pattern.compile(PASSWORD_REGEX);
            Matcher matcher = pattern.matcher(password);
            return matcher.matches();
        }else{
            return false;
        }
    }
}
