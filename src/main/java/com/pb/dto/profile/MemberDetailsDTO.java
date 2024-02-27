package com.pb.dto.profile;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.pb.constants.ValidationMessageConstant;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldDefaults;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.constraints.NotNull;

@FieldDefaults(level = AccessLevel.PRIVATE)
@Getter
@Setter
@JsonInclude(JsonInclude.Include.NON_NULL)
public class MemberDetailsDTO {


    @NotNull(message = ValidationMessageConstant.PLEASE_ENTER_VALID_USER)
    Long userId;
    Boolean isBroker;
    String title;
    String name;
    String phone;
    String country;
    String address;
    String email;
    String website;
    Long memberTimezoneId;
    Long departmentId;
    MultipartFile profilePic;
}
