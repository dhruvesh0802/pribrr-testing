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
public class ArtworkDetailsResDTO {

    Integer Section;// 1,2,3,4,5
    @NotNull(message = ValidationMessageConstant.PLEASE_ENTER_VALID_USER)
    Long userId;
    List<String> productCatalogPath;
    String artworkLabel; // 1,2,3,4 comma seprate
    String adAgencyName; // if 2 option selected in artwork label
    Boolean artworkRequirements;
    Long artworkCostPreference; // 1,2,3,4
    String artworkRequirementPath;
    String printerSpecsPath;
    String dielinesPath;
    String artworkContact; // phone
    String artworkEmail;
}
