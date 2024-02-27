package com.pb.validation;

import com.pb.validation.handler.ValidPasswordHandler;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.*;

@Documented
@Constraint(validatedBy = ValidPasswordHandler.class)
@Target({ElementType.FIELD,ElementType.METHOD,ElementType.PARAMETER
        ,ElementType.ANNOTATION_TYPE})
@Retention(RetentionPolicy.RUNTIME)
public @interface FileSizeValidation {
    String message() default "MAX FILE SIZE EXCEED";
    Class<?>[] groups() default {};
    Class<? extends Payload> [] payload() default{};
}
