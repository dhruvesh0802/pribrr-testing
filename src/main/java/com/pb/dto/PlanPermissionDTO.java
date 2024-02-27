package com.pb.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldDefaults;

import java.util.List;

@Getter
@Setter
@FieldDefaults(level = AccessLevel.PRIVATE)
public class PlanPermissionDTO {
    
    @JsonProperty("plan_id")
    Long planId;
    @JsonProperty("permissions_ids")
    List<Long> permissionsIds;
}
