package com.pb.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.pb.constants.UserType;
import com.pb.constants.ValidationMessageConstant;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldDefaults;

import javax.persistence.Column;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.List;


@FieldDefaults(level = AccessLevel.PRIVATE)
@Getter
@Setter
public class ProductDTO {
    
    Long id;

    @NotEmpty(message = ValidationMessageConstant.PLEASE_ENTER_NAME)
    String name;

    @NotNull(message = ValidationMessageConstant.PLEASE_ENTER_DEPARTMENT)
    Long departmentId;

    @NotNull(message = ValidationMessageConstant.PLEASE_ENTER_CATEGORY)
    Long categoryId;

    @NotNull(message = ValidationMessageConstant.PLEASE_ENTER_VALID_USER)
    Long userId;

    String description;

    String minimumOrderQuantity;

    Integer moqType;

    String caseDimension;

    String caseWeight;

    String avgUnitSize;

    String avgUnitWeight;

    List<String> deletedFiles;



}
