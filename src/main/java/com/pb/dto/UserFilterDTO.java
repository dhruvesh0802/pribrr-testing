package com.pb.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.pb.constants.UserType;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldDefaults;

@FieldDefaults(level = AccessLevel.PRIVATE)
@Getter
@Setter
@JsonInclude(JsonInclude.Include.NON_NULL)
public class UserFilterDTO {
    
    Long id;
    String searchKeyword;
    UserType userType;

}
