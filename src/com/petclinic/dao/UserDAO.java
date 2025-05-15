package com.petclinic.dao;

import com.petclinic.model.User;
import java.sql.SQLException;

public interface UserDAO {
    User findById(String id) throws SQLException;
    User findByEmail(String email) throws SQLException;
    String create(User user) throws SQLException;
    boolean update(User user) throws SQLException;
    boolean delete(String id) throws SQLException;
    boolean authenticate(String id, String password) throws SQLException;
    String getNextId() throws SQLException;
}