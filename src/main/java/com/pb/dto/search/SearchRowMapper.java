package com.pb.dto.search;


import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public class SearchRowMapper implements RowMapper<SearchDTO> {
    @Override
    public SearchDTO mapRow(ResultSet rs, int rowNum) throws SQLException {
        SearchDTO search = new SearchDTO();
        search.setUserId(rs.getLong("userId"));
        search.setFirstName(rs.getString("firstName"));
        return search;
    }
}