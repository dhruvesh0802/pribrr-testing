package com.pb.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.pb.constants.ValidationMessageConstant;
import com.pb.validation.ValidPassword;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldDefaults;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.List;

@FieldDefaults(level = AccessLevel.PRIVATE)
@Getter
@Setter
@JsonInclude(JsonInclude.Include.NON_NULL)
public class UpdateUserDTO extends BaseDTO{
    
    String name;
    @NotEmpty(message = ValidationMessageConstant.PLEASE_ENTER_EMAIL)
    @Email(message = ValidationMessageConstant.PLEASE_PROVIDE_VALID_EMAIL)
    String email;
    @NotEmpty(message = ValidationMessageConstant.PLEASE_ENTER_PASSWORD)
    @ValidPassword
    String password;
    @JsonProperty(value = "device_token")
    String deviceToken;
    @JsonProperty(value = "platform_type")
    String platformType;
    @JsonProperty(value = "auth_token")
    String authToken;
    @NotEmpty(message = ValidationMessageConstant.PLEASE_ENTER_MOBILE_NUMBER)
    String mobileNo;
    
    @NotNull(message = ValidationMessageConstant.PLEASE_ENTER_COUNTRY_ID)
    CountryDTO country;
    @NotNull(message = ValidationMessageConstant.PLEASE_ENTER_ROLE)
    List<Long> roleIds;
    
    
}

