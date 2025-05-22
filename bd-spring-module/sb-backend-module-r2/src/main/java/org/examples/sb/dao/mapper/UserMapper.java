package org.examples.sb.dao.mapper;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.examples.sb.models.User;
import org.springframework.jdbc.core.RowMapper;

public class UserMapper implements RowMapper<User> {
    
    @Override
    public User mapRow(ResultSet rs, int rowNum) throws SQLException {
        User user = new User();
        user.setId(rs.getLong("id"));
        user.setName(rs.getString("name"));
        user.setEmail(rs.getString("email"));
        return user;
    }
    
}
