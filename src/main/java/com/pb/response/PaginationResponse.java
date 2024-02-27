package com.pb.response;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldDefaults;

import java.io.Serializable;
import java.util.List;

@FieldDefaults(level = AccessLevel.PRIVATE)
@Getter
@Setter
public class PaginationResponse implements Serializable {

    private static final long serialVersionUID = 71565260778832L;
    List<?> content;
    long totalElements;
    int totalPage;
    int pageSize;
    
}
