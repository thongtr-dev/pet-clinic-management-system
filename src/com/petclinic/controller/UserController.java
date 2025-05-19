package com.petclinic.controller;

import com.petclinic.dao.UserDAO;
import com.petclinic.model.User;
import com.petclinic.util.SessionManager;
import com.petclinic.view.UserView;
import java.sql.SQLException;

public class UserController {
    private UserView userView;
    private UserDAO userDAO;
    private SessionManager sessionManager;
    private NavigationController navigationController;
    
    public UserController(UserView userView, UserDAO userDAO) {
        this.userView = userView;
        this.userDAO = userDAO;
        this.sessionManager = SessionManager.getInstance();
        attachEventListeners();
    }

    public void setNavigationController(NavigationController navigationController) { this.navigationController = navigationController; }
    
    private void attachEventListeners() {
        userView.getLoginButton().addActionListener(e -> {
            if (userView.isLoginMode()) {
                handleLogin();
            }
            else {
                userView.showLogin();
            }
        });
        userView.getRegisterButton().addActionListener(e -> {
            if (userView.isLoginMode()) {
                userView.showRegister();
            }
            else {
                handleRegister();
            }
        });
        userView.getLogoutButton().addActionListener(e -> handleLogout());
        userView.getPetButton().addActionListener(e -> {
            if (navigationController != null) {
                navigationController.showPetView();
            }
        });
        
        userView.getOwnerButton().addActionListener(e -> {
        if (navigationController != null) {
            navigationController.showOwnerView();
        }
    });
    }
    
    private void handleLogin() {
        String username = userView.getUsername();
        String password = userView.getPassword();
        if (username.isEmpty() || password.isEmpty()) {
            userView.showMessage("Hãy nhập đầy đủ thông tin đăng nhập!");
            return;
        }
        try {
            User user = userDAO.verify(username, password);
            if (user != null) {
                sessionManager.login(user);
                userView.showDashboard();
            }
            else {
                userView.showMessage("Tài khoản hoặc mật khẩu không hợp lệ!");
            }
        }
        catch (SQLException ex) {
            userView.showMessage("Database error: " + ex.getMessage());
        }
    }
    
    private void handleRegister() {
        String username = userView.getUsername();
        String password = userView.getPassword();
        if (username.isEmpty() || password.isEmpty()) {
            userView.showMessage("Hãy nhập đầy đủ thông tin đăng ký!");
            return;
        }
        try {
            User newUser = new User(username, password);
            boolean isRegistered = userDAO.add(newUser);
            if (isRegistered) {
                userView.showMessage("Đăng ký tài khoản thành công!");
                User user = userDAO.verify(username, password);
                sessionManager.login(user);
                userView.showDashboard();
            }
            else {
                userView.showMessage("Tài khoản đã tồn tại!");
            }
        }
        catch (SQLException ex) {
            userView.showMessage("Database error: " + ex.getMessage());
        }
    }

    private void handleLogout() {
        sessionManager.logout();
        userView.showLogin();
    }
}