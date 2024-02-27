package com.pb.response;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;
import lombok.experimental.FieldDefaults;
import org.springframework.http.HttpStatus;

@Getter
@Setter
@FieldDefaults(level = AccessLevel.PRIVATE)
@NoArgsConstructor
public class CustomResponse {
    
    @JsonIgnore
    HttpStatus httpStatus;
    Object data;
    String message;
    int status;
    
    public CustomResponse( HttpStatus httpStatus,String message,Object data) {
        this.status = httpStatus.value();
        this.message = message;
        this.data = data;
    }
    
    public CustomResponse( HttpStatus httpStatus,String message) {
        this.status = httpStatus.value();
        this.message = message;
    }
}
