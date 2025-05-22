package org.examples.sb.dao.impl;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.examples.sb.dao.UserDAO;
import org.examples.sb.dao.mapper.UserMapper;
import org.examples.sb.models.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementSetter;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

@Repository
public class UserDAOImpl implements UserDAO {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    // RowMapper to map result set to Student object
    private final RowMapper<User> rowMapper = (rs, rowNum) -> {
        User user = new User();
        user.setId(rs.getLong("id"));
        user.setName(rs.getString("name"));
        user.setEmail(rs.getString("email"));
        return user;
    };

    @Override
    public void createStudent(User student) throws DataAccessException {
        String sql = "INSERT INTO Student (id, name, age) VALUES (?, ?, ?)";
        jdbcTemplate.update(sql, student.getId(), student.getName(), student.getStatus());
    }

    @Override
    public User getStudentById(Integer id) throws DataAccessException {
        String sql = "SELECT * FROM Student WHERE id = ?";
        // jdbcTemplate.query(sql, new UserResultSetExtractor());
        return jdbcTemplate.queryForObject(sql, rowMapper, id);
    }

    @Override
    public List<User> listStudents() throws DataAccessException {
        String sql = "SELECT * FROM Student";
        return jdbcTemplate.query(sql, rowMapper);
    }

    @Override
    public int updateStudent(User student) throws DataAccessException {
        String sqlQuery = "UPDATE student SET name = ? WHERE id = ?";
        try {
            // Execute the update query
            jdbcTemplate.update(sqlQuery, student.getName(), student.getId());
            return 1;
        } catch (DataAccessException e) {
            // Handle database-related exceptions
            System.err.println("Error updating student: " + e.getMessage());
            return 0; // Return 0 to indicate failure
        }
    }

    public List<User> getUsersByName(final String name) {
        String sql = "SELECT * FROM frameworks WHERE name = ?";
        
        List<User> users = jdbcTemplate.query(sql, new PreparedStatementSetter() {
                public void setValues(PreparedStatement ps) throws SQLException {
                    ps.setString(1, name);
                }
            },
            new UserMapper()
        );
        return users;
    }

    @Override
    public void deleteStudent(Integer id) throws DataAccessException {
        String sql = "DELETE FROM Student WHERE id = ?";
        jdbcTemplate.update(sql, id);
    }

}
