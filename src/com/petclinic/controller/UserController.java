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
    
    public UserController(UserView userView, UserDAO userDAO) {
        this.userView = userView;
        this.userDAO = userDAO;
        this.sessionManager = SessionManager.getInstance();
        attachEventListeners();
    }
    
    private void attachEventListeners() {
        userView.getLoginButton().addActionListener(e -> {
            if (userView.isLoginMode()) {
                handleLogin();
            }
            else {
                clearFields();
                showLoginView();
            }
        });
        userView.getRegisterButton().addActionListener(e -> {
            if (userView.isLoginMode()) {
                clearFields();
                showRegisterView();
            }
            else {
                handleRegister();
            }
        });
        userView.getLogoutButton().addActionListener(e -> handleLogout());
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
                clearFields();
                showMainView(user);
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
                clearFields();
                showMainView(user);
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
        clearFields();
        showLoginView();
    }
    
    public void showLoginView() {
        userView.setLoginMode(true);
        userView.setTitleLabel("ĐĂNG NHẬP TÀI KHOẢN");
        userView.switchToLoginPanel();
    }
    
    public void showRegisterView() {
        userView.setLoginMode(false);
        userView.setTitleLabel("ĐĂNG KÝ TÀI KHOẢN");
        userView.switchToLoginPanel();
    }
    
    public void showMainView(User user) { userView.switchToMainPanel(); }
    
    public void clearFields() {
        userView.setUsername("");
        userView.setPassword("");
    }
}