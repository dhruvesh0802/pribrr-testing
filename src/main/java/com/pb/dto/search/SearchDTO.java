package com.pb.dto.search;

import lombok.Data;

@Data
public class SearchDTO {
    Long userId;
    String firstName;
    String companyName;
    String companyLogo;
    Boolean inNetwork;
    String memberName;
    String profilePath;
}
