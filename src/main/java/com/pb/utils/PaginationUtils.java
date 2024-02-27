package com.pb.utils;

import com.pb.response.PaginationResponse;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class PaginationUtils {
    
    public static PaginationResponse getPaginationResponse(Page<?> page, boolean isFromMobile) {
        PaginationResponse paginationResponse = new PaginationResponse();
        
        paginationResponse.setContent(page.getContent());
        paginationResponse.setTotalElements(page.getTotalElements());
        paginationResponse.setTotalPage(page.getTotalPages());
        paginationResponse.setPageSize(page.getPageable().getPageSize());

        return paginationResponse;
    }
    
    public static Pageable getPageRequestOfMaxInteger() {
        return PageRequest.of(0, Integer.MAX_VALUE, Sort.by(Sort.Direction.DESC, "createdDate"));
    }
}
