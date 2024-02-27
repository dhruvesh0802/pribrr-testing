package com.pb.dto.profile;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.pb.constants.ValidationMessageConstant;
import com.pb.model.PrivateBrandEntity;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldDefaults;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.constraints.NotNull;
import java.util.List;

@FieldDefaults(level = AccessLevel.PRIVATE)
@Getter
@Setter
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ProductDetailsDTO {

    Integer Section;// 1,2,3,4,5
    @NotNull(message = ValidationMessageConstant.PLEASE_ENTER_VALID_USER)
    Long userId;

    Integer minimumOrder; // 1-Case pack,2-Units
    Integer averageTimeRequire; // 1-30,2-60,3-90,4-120
    Integer pickupRequired;
    String pickupDetails;
    String pickupPoints;
    Boolean EDI;
    Boolean workWithAllowance;
    Boolean workWithDeadnet;
    MultipartFile factoryAuditReport;
    MultipartFile certificateOfInsurance;
    List<MultipartFile> otherCertificate;
    List<String> deletedOtherCertificate;
    MultipartFile documentAndForum;
    MultipartFile NDA;
    List<PrivateBrandDto> privateBrand;
    List<Long> deletedPrivateBrand;
    String otherLink;
}
