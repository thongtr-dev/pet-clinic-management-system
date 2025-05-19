package com.petclinic.view;

import java.awt.*;
import javax.swing.*;

public class UserView extends JFrame {
    private JTextField usernameField;
    private JPasswordField passwordField;
    private JButton loginButton;
    private JButton registerButton;
    private JButton logoutButton;
    private JPanel authenticationPanel;
    private JPanel dashboardPanel;
    private JLabel titleLabel;
    private JLabel welcomeLabel;
    private boolean isLoginMode = true;
    private JButton petButton;
    private JButton ownerButton;

    public UserView() {
        setTitle("HỆ THỐNG QUẢN LÝ PHÒNG KHÁM THÚ CƯNG");
        setSize(500, 300);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        initializeAuthenticationPanel();
        initializeDashboardPanel();
        setContentPane(authenticationPanel);
    }

    private void initializeAuthenticationPanel() {
        authenticationPanel = new JPanel();
        authenticationPanel.setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        titleLabel = new JLabel("ĐĂNG NHẬP TÀI KHOẢN");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 20));
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2;
        authenticationPanel.add(titleLabel, gbc);
        JLabel usernameLabel = new JLabel("TÀI KHOẢN:");
        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.gridwidth = 1;
        gbc.anchor = GridBagConstraints.WEST;
        authenticationPanel.add(usernameLabel, gbc);
        usernameField = new JTextField(20);
        gbc.gridx = 1;
        gbc.gridy = 1;
        authenticationPanel.add(usernameField, gbc);
        JLabel passwordLabel = new JLabel("MẬT KHẨU:");
        gbc.gridx = 0;
        gbc.gridy = 2;
        authenticationPanel.add(passwordLabel, gbc);
        passwordField = new JPasswordField(20);
        gbc.gridx = 1;
        gbc.gridy = 2;
        authenticationPanel.add(passwordField, gbc);
        JPanel buttonPanel = new JPanel();
        loginButton = new JButton("ĐĂNG NHẬP");
        registerButton = new JButton("ĐĂNG KÝ");
        buttonPanel.add(loginButton);
        buttonPanel.add(registerButton);
        gbc.gridx = 0;
        gbc.gridy = 3;
        gbc.gridwidth = 2;
        gbc.anchor = GridBagConstraints.CENTER;
        authenticationPanel.add(buttonPanel, gbc);
    }

    private void initializeDashboardPanel() {
        dashboardPanel = new JPanel();
        dashboardPanel.setLayout(new BorderLayout());
        JPanel headerPanel = new JPanel(new BorderLayout());
        welcomeLabel = new JLabel("BẢNG ĐIỀU KHIỂN");
        welcomeLabel.setFont(new Font("Arial", Font.BOLD, 18));
        welcomeLabel.setHorizontalAlignment(JLabel.LEFT);
        headerPanel.add(welcomeLabel, BorderLayout.CENTER);
        logoutButton = new JButton("ĐĂNG XUẤT");
        headerPanel.add(logoutButton, BorderLayout.EAST);
        dashboardPanel.add(headerPanel, BorderLayout.NORTH);
        JPanel functionsPanel = new JPanel(new GridLayout(2, 2, 10, 10));
        functionsPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        petButton = new JButton("QUẢN LÝ HỒ SƠ THÚ CƯNG");
        ownerButton = new JButton("QUẢN LÝ HỒ SƠ CHỦ NUÔI");
        JButton appointmentButton = new JButton("QUẢN LÝ LỊCH HẸN");
        JButton medicalButton = new JButton("QUẢN LÝ HỒ SƠ Y TẾ");
        functionsPanel.add(petButton);
        functionsPanel.add(ownerButton);
        functionsPanel.add(appointmentButton);
        functionsPanel.add(medicalButton);
        dashboardPanel.add(functionsPanel, BorderLayout.CENTER);
    }
    
    public void showLogin() {
        clearFields();
        isLoginMode = true;
        titleLabel.setText("ĐĂNG NHẬP TÀI KHOẢN");
        setContentPane(authenticationPanel);
        revalidate();
        repaint();
    }

    public void showRegister() {
        clearFields();
        isLoginMode = false;
        titleLabel.setText("ĐĂNG KÝ TÀI KHOẢN");
        setContentPane(authenticationPanel);
        revalidate();
        repaint();
    }

    public void showDashboard() {
        setContentPane(dashboardPanel);
        revalidate();
        repaint();
    }

    public void clearFields() {
        passwordField.setText("");
        usernameField.setText("");
    }
    
    public void showMessage(String message) { JOptionPane.showMessageDialog(this, message); }
    
    public String getUsername() { return usernameField.getText(); }
    
    public String getPassword() { return new String(passwordField.getPassword()); }
    
    public boolean isLoginMode() { return isLoginMode; }

    public JButton getLoginButton() { return loginButton; }
    
    public JButton getRegisterButton() { return registerButton; }
    
    public JButton getLogoutButton() { return logoutButton; }

    public JButton getPetButton() { return petButton; }

    public JButton getOwnerButton() { return ownerButton; }
}