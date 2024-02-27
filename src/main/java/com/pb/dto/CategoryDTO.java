package com.pb.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class CategoryDTO {
    Long id;
    String name;
    
    @JsonProperty(value = "department_id")
    Long departmentId;
    
    boolean isActive=true;
}
