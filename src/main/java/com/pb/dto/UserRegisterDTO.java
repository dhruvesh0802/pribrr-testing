package com.pb.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.pb.constants.UserType;
import com.pb.constants.ValidationMessageConstant;
import com.pb.validation.ValidPassword;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldDefaults;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

@FieldDefaults(level = AccessLevel.PRIVATE)
@Getter
@Setter
@JsonInclude(JsonInclude.Include.NON_NULL)
public class UserRegisterDTO {

    Long id;

    @JsonProperty(value = "user_type")
    @NotNull(message = ValidationMessageConstant.PLEASE_ENTER_USER_TYPE)
    UserType userType;
    
    @JsonProperty(value = "company_name")
    String companyName;
    
    @JsonProperty(value = "user_name")
    String userName;
    
    @NotEmpty(message = ValidationMessageConstant.PLEASE_ENTER_EMAIL)
    @Email(message = ValidationMessageConstant.PLEASE_PROVIDE_VALID_EMAIL)
    String email;
    @NotEmpty(message = ValidationMessageConstant.PLEASE_ENTER_PASSWORD)
    @ValidPassword
    String password;
    
    @JsonProperty(value = "confirm_password")
    @NotEmpty(message = ValidationMessageConstant.PLEASE_ENTER_PASSWORD)
    @ValidPassword
    String confirmPassword;

    MultipartFile profilePic;
    
}
