package com.pb.dto.search;

import org.springframework.jdbc.core.PreparedStatementSetter;

import java.sql.PreparedStatement;
import java.sql.SQLException;

public class SearchPreparedStatementSetter implements PreparedStatementSetter {
    String role;
    Integer departmentId;

    public SearchPreparedStatementSetter( String role,Integer departmentId){
        this.role = role;
        this.departmentId = departmentId;
    }
    @Override
    public void setValues(PreparedStatement ps) throws SQLException {
        ps.setString(1, role);
        ps.setLong(2,departmentId);
    }
}
