package com.petclinic.controller;

import com.petclinic.dao.UserDAO;
import com.petclinic.dao.UserDAOImpl;
import com.petclinic.model.User;
import com.petclinic.util.SessionManager;
import com.petclinic.view.LoginView;
import com.petclinic.view.MainView;
import javax.swing.*;
import java.sql.SQLException;

public class UserController {
    private UserDAO userDAO;
    private SessionManager sessionManager;

    public UserController() {
        this.userDAO = new UserDAOImpl();
        this.sessionManager = SessionManager.getInstance();
    }

    public boolean login(String userId, String password) {
        try {
            boolean authenticated = userDAO.authenticate(userId, password);
            if (authenticated) {
                User user = userDAO.findById(userId);
                sessionManager.login(user);
                return true;
            }
            return false;
        }
        catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Lỗi khi đăng nhập: " + e.getMessage(), "LỖI", JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
            return false;
        }
    }

    public void logout() {
        sessionManager.logout();
        LoginView loginView = new LoginView(this);
        loginView.setVisible(true);
    }

    public String register(String name, String email, String password) {
        try {
            User existingUser = userDAO.findByEmail(email);
            if (existingUser != null) {
                JOptionPane.showMessageDialog(null, "Email đã được sử dụng.", "LỖI", JOptionPane.ERROR_MESSAGE);
                return null;
            }
            User newUser = new User();
            newUser.setName(name);
            newUser.setEmail(email);
            newUser.setPassword(password);
            String userId = userDAO.create(newUser);
            return userId;
        }
        catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Lỗi khi đăng ký: " + e.getMessage(), "LỖI", JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
            return null;
        }
    }

    public void showMainView() {
        MainView mainView = new MainView(this);
        mainView.setVisible(true);
    }
}