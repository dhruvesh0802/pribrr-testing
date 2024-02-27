package com.pb.dto;

import com.pb.constants.ValidationMessageConstant;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldDefaults;

import javax.validation.constraints.NotNull;

@Getter
@Setter
@FieldDefaults(level = AccessLevel.PRIVATE)
public class PlanDTO extends BaseDTO{
    @NotNull(message = ValidationMessageConstant.PLEASE_ENTER_NAME)
    String name;
    @NotNull(message = ValidationMessageConstant.PLEASE_ENTER_PRICE)
    String benefits;
    @NotNull(message = ValidationMessageConstant.PLEASE_ENTER_BENEFITS)
    Float price;
    
}
