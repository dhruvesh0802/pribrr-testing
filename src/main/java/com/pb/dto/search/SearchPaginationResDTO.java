package com.pb.dto.search;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
public class SearchPaginationResDTO {
    List<SearchDTO> data;
    int totalPages;
}
