package com.pb.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.pb.constants.UserType;
import com.pb.constants.ValidationMessageConstant;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldDefaults;

import javax.persistence.Column;
import javax.persistence.Lob;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;


@FieldDefaults(level = AccessLevel.PRIVATE)
@Getter
@Setter
public class StaticPageDTO {

    Long id;

    String pageName;

    String content;
}
