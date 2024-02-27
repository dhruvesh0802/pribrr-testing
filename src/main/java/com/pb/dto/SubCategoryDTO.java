package com.pb.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class SubCategoryDTO {
    Long id;
    String name;
    
    @JsonProperty(value = "category_id")
    Long categoryId;
    
    boolean isActive=true;
}
