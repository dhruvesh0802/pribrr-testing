package com.pb.dto;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldDefaults;

@Getter
@Setter
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ParameterDTO {
    Long departmentId;
    Long categoryId;
    String userType;
    String key;
    Integer pageNumber;
    Boolean forNetwork;
    Long userId;
}
