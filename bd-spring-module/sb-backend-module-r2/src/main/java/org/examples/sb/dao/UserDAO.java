package org.examples.sb.dao;

import java.util.List;

import org.examples.sb.models.User;
import org.springframework.dao.DataAccessException;

public interface UserDAO {

    // Create: Insert a new student record
    void createStudent(User student) throws DataAccessException;

    // Read: Retrieve a student by ID
    User getStudentById(Integer id) throws DataAccessException;

    // Read: List all students
    List<User> listStudents() throws DataAccessException;

    // Update: Update an existing student record
    int updateStudent(User student) throws DataAccessException;

    // Delete: Delete a student by ID
    void deleteStudent(Integer id) throws DataAccessException;

}
