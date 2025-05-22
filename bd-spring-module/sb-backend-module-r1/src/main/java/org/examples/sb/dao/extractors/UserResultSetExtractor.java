package org.examples.sb.dao.extractors;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.examples.sb.models.User;
import org.springframework.jdbc.core.ResultSetExtractor;

public class UserResultSetExtractor implements ResultSetExtractor<List<User>> {
        @Override
        public List<User> extractData(ResultSet rs) throws SQLException {
            List<User> students = new ArrayList<>();
            while (rs.next()) {
                int id = rs.getInt("id");
                String name = rs.getString("name");
                String department = rs.getString("department");
                User user = User.builder().email(department).build();
                students.add(user);
            }
            return students;
        }
    }