package com.pb.dto.profile.response;

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
public class CompanyDetailsResDTO {

    Integer Section;// 1,2,3,4,5
    @NotNull(message = ValidationMessageConstant.PLEASE_ENTER_VALID_USER)
    Long userId;

    String companyName;
    Long businessType; //  1-Discount Chain, 2-General-Department Store, 3-Grocery, 4-Convenient Store, 5-Pet Supplies, 6-Other
    Long categoryId;
    String companyLocation; // Country
    String companyAddress;
    String companyPhone;
    String companyEmail;
    String companyWebsite;
    Long companyTimezoneId;
    String companyLogoURL;
    Long companyType; // 1-LLC,2-Public,3-partnership,4-Sole propriter
    String ceoName;
    Double annualSale;
    String annualRevenue;
    String noOfEmployee;
    String keyCompetitors;
    String noOfLocations;
}
