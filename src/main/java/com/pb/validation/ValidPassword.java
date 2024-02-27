package com.pb.validation;



import com.pb.validation.handler.ValidPasswordHandler;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.*;

/**
 *  When password entered, Below condition must be satisfied.
 *  1. Eight character
 *  1. One Upper letter
 *  2. One Number
 *  3. One special character
 */
@Documented
@Constraint(validatedBy = ValidPasswordHandler.class)
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
public @interface ValidPassword {

    String message() default "INVALID PASSWORD";
    Class<?>[] groups() default {};
    
    Class<? extends Payload>[] payload() default {};
}
