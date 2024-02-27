package com.pb.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldDefaults;
import java.time.LocalDateTime;

@FieldDefaults(level = AccessLevel.PRIVATE)
@Getter
@Setter
public class BaseDTO {
    @JsonProperty(value = "id")
    Long id;
    
    @JsonProperty(value = "created_date")
    LocalDateTime createdDate;
    @JsonProperty(value = "updated_date")
    LocalDateTime updatedDate;
    
    @JsonProperty(value = "is_active")
    Boolean isActive=true;
}
