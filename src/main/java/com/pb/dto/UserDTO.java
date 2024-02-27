package com.pb.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.pb.validation.ValidPassword;
import lombok.*;
import lombok.experimental.FieldDefaults;

import javax.persistence.Column;
import javax.validation.constraints.Email;

@FieldDefaults(level = AccessLevel.PRIVATE)
@Getter
@Setter
@JsonInclude(JsonInclude.Include.NON_NULL)
public class UserDTO {

    Long id;
    String name;
    @Email(message = "PLEASE PROVIDE VALID EMAIL")
    String email;
    @ValidPassword
    String password;
    @JsonProperty(value = "device_token")
    String deviceToken;
    @JsonProperty(value = "platform_type")
    String platformType;
    @JsonProperty(value = "role")
    String role;
    @JsonProperty(value = "auth_token")
    String authToken;
    String mobileNo;
    Long countryId;
    String firstName;
    String lastName;
    @JsonProperty(value = "user_type")
    String userType;
    String userName;
    Boolean isVerified;
    String profilePath;
    Boolean isRemember;
}
