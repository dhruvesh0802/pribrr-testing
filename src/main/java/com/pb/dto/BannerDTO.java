package com.pb.dto;

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
public class BannerDTO {
    
    Long id;
    @NotEmpty(message = ValidationMessageConstant.PLEASE_ENTER_TITLE)
    String title;
    @NotEmpty(message = ValidationMessageConstant.PLEASE_ENTER_REDIRECT_URL)
    String redirectUrl;
    String bannerPath;
    
    @JsonProperty(value = "location")
    @NotNull(message = ValidationMessageConstant.PLEASE_ENTER_LOCATION)
    String location;

    @JsonProperty(value = "description")
    @NotNull(message = ValidationMessageConstant.PLEASE_ENTER_DESCRIPTION)
    String description;
}
