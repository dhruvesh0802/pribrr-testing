package com.pb.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.pb.constants.ValidationMessageConstant;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldDefaults;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

@FieldDefaults(level = AccessLevel.PRIVATE)
@Getter
@Setter
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ChangePasswordDTO {
    @NotNull(message = ValidationMessageConstant.PLEASE_ENTER_VALID_USER)
    Long userId;

    @NotEmpty(message = ValidationMessageConstant.PLEASE_ENTER_OLD_PASSWORD)
    @JsonProperty(value = "old_password")
    String oldPassword;

    @NotEmpty(message = ValidationMessageConstant.PLEASE_ENTER_PASSWORD)
    String password;

    @NotEmpty(message = ValidationMessageConstant.PLEASE_ENTER_CONFIRM_PASSWORD)
    @JsonProperty(value = "confirm_password")
    String confirmPassword;
}
