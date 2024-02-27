package com.pb.dto.profile.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.pb.constants.ValidationMessageConstant;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldDefaults;

import javax.validation.constraints.NotNull;

@FieldDefaults(level = AccessLevel.PRIVATE)
@Getter
@Setter
@JsonInclude(JsonInclude.Include.NON_NULL)
public class QADetailsResDTO {

    Integer Section;// 1,2,3,4,5
    @NotNull(message = ValidationMessageConstant.PLEASE_ENTER_VALID_USER)
    Long userId;

    String qaContact; // phone
    String qaEmail;
    Integer qaLab; //1,2,3 - other
    String qaLabName;// if other selected in QaLab
    String sourcingPartners; // comma seprate String
}
