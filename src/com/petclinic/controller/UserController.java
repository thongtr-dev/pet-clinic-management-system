package com.petclinic.controller;

import com.petclinic.dao.UserDAO;
import com.petclinic.model.User;
import com.petclinic.util.SessionManager;
import com.petclinic.view.UserView;

import java.sql.SQLException;

public class UserController {
    private final UserView userView;
    private final UserDAO userDAO;
    private final SessionManager sessionManager;
    private final NavigationController navigationController;

    public UserController(UserView userView, UserDAO userDAO, NavigationController navigationController) {
        this.userView = userView;
        this.userDAO = userDAO;
        this.sessionManager = SessionManager.getInstance();
        this.navigationController = navigationController;
        attachEventListeners();
    }

    private void attachEventListeners() {
        userView.getLoginButton().addActionListener(e -> handleLogin());
        userView.getRegisterButton().addActionListener(e -> handleRegister());
        userView.getLogoutButton().addActionListener(e -> handleLogout());
        userView.getPetButton().addActionListener(e -> showPetView());
        userView.getOwnerButton().addActionListener(e -> showOwnerView());
        userView.getAppointmentButton().addActionListener(e -> showAppointmentView());
        userView.getMedicalRecordButton().addActionListener(e -> showMedicalRecordView());
        userView.getSearchButton().addActionListener(e -> showSearchView());
    }

    private void handleLogin() {
        if (userView.isLoginMode()) {
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
                } else {
                    userView.showMessage("Tài khoản hoặc mật khẩu không hợp lệ!");
                }
            } catch (SQLException ex) {
                userView.showMessage("Database error: " + ex.getMessage());
            }
        } else {
            userView.showLogin();
        }
    }

    private void handleRegister() {
        if (userView.isLoginMode()) {
            userView.showRegister();
        } else {
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
                } else {
                    userView.showMessage("Tài khoản đã tồn tại!");
                }
            } catch (SQLException ex) {
                userView.showMessage("Database error: " + ex.getMessage());
            }
        }
    }

    private void handleLogout() {
        sessionManager.logout();
        userView.showLogin();
    }

    private void showPetView() {
        if (navigationController != null) {
            navigationController.navigateTo("PET_VIEW");
        }
    }

    private void showOwnerView() {
        if (navigationController != null) {
            navigationController.navigateTo("OWNER_VIEW");
        }
    }

    private void showAppointmentView() {
        if (navigationController != null) {
            navigationController.navigateTo("APPOINTMENT_VIEW");
        }
    }

    private void showMedicalRecordView() {
        if (navigationController != null) {
            navigationController.navigateTo("MEDICAL_RECORD_VIEW");
        }
    }

    private void showSearchView() {
        if (navigationController != null) {
            navigationController.navigateTo("SEARCH_VIEW");
        }
    }
}