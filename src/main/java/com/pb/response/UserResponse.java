package com.pb.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.pb.dto.BaseDTO;
import com.pb.dto.CountryDTO;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldDefaults;

@FieldDefaults(level = AccessLevel.PRIVATE)
@Getter
@Setter
@JsonInclude(JsonInclude.Include.NON_NULL)
public class UserResponse extends BaseDTO {
    String name;
    String email;
    @JsonProperty(value = "platform_type")
    String platformType;
    String mobileNo;
    CountryDTO country;
    
    @JsonProperty(value = "user_type")
    String userType;
    
    @JsonProperty(value = "first_name")
    String firstName;
    @JsonProperty(value = "last_name")
    String lastName;
    
    @JsonProperty(value = "company_name")
    String companyName;
    
    @JsonProperty(value = "user_name")
    String userName;
    String profileUrl;
}
