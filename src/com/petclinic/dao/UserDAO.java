package com.petclinic.dao;

import com.petclinic.model.User;

import java.sql.SQLException;

public interface UserDAO {
    boolean add(User user) throws SQLException;

    boolean isUsernameTaken(String username) throws SQLException;

    User verify(String username, String password) throws SQLException;
}