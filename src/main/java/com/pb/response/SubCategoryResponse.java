package com.pb.response;

import com.pb.dto.CategoryDTO;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldDefaults;

@Getter
@Setter
@FieldDefaults(level = AccessLevel.PRIVATE)
public class SubCategoryResponse {
    Long id;
    String name;
    Boolean isActive;
    CategoryDTO categoryDTO;
}
