package com.pb.dto.profile.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.pb.constants.ValidationMessageConstant;
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
public class ProductDetailsResDTO {

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
    String factoryAuditReportPath;
    String certificateOfInsurancePath;
    List<String> otherCertificatePath;
    String documentAndForumPath;
    String NDAPath;
    List<PrivateBrandResDto> privateBrand;
    String otherLink;
}
