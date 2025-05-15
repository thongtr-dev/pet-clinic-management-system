package com.petclinic.view;

import com.petclinic.controller.UserController;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

public class LoginView extends JFrame {
    private UserController userController;
    private JTextField txtUserId;
    private JPasswordField txtPassword;
    private JButton btnLogin;
    private JButton btnRegister;
    private JLabel lblTitle;

    public LoginView(UserController userController) {
        this.userController = userController;
        initComponents();
    }

    private void initComponents() {
        setTitle("Hệ Thống Quản Lý Phòng Khám Thú Cưng");
        setSize(450, 300);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setResizable(false);
        // Đăng Nhập Panel
        JPanel mainPanel = new JPanel(new BorderLayout(10, 10));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        lblTitle = new JLabel("Đăng Nhập Tài Khoản", SwingConstants.CENTER);
        lblTitle.setFont(new Font("Arial", Font.BOLD, 18));
        mainPanel.add(lblTitle, BorderLayout.NORTH);
        JPanel inputPanel = new JPanel(new GridLayout(2, 2, 10, 10));
        JLabel lblUserId = new JLabel("Tài Khoản:");
        txtUserId = new JTextField(20);
        txtUserId.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                    login();
                }
            }
        });
        JLabel lblPassword = new JLabel("Mật Khẩu:");
        txtPassword = new JPasswordField(20);
        txtPassword.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                    login();
                }
            }
        });
        inputPanel.add(lblUserId);
        inputPanel.add(txtUserId);
        inputPanel.add(lblPassword);
        inputPanel.add(txtPassword);
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 10));
        // Nút Đăng Nhập
        btnLogin = new JButton("Đăng Nhập");
        btnLogin.addActionListener((ActionEvent e) -> {
            login();
        });
        btnLogin.setPreferredSize(new Dimension(120, 40));
        // Nút Đăng Ký
        btnRegister = new JButton("Đăng Ký");
        btnRegister.addActionListener((ActionEvent e) -> {
            dispose();
            RegisterView registerView = new RegisterView(userController);
            registerView.setVisible(true);
        });
        btnRegister.setPreferredSize(new Dimension(120, 40));
        buttonPanel.add(btnLogin);
        buttonPanel.add(btnRegister);
        JPanel centerPanel = new JPanel(new BorderLayout(0, 20));
        centerPanel.add(inputPanel, BorderLayout.CENTER);
        centerPanel.add(buttonPanel, BorderLayout.SOUTH);
        mainPanel.add(centerPanel, BorderLayout.CENTER);
        add(mainPanel);
    }

    private void login() {
        String userId = txtUserId.getText().trim();
        String password = new String(txtPassword.getPassword());
        if (userId.isEmpty() || password.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Vui lòng nhập đầy đủ thông tin đăng nhập.", "THÔNG BÁO", JOptionPane.WARNING_MESSAGE);
            return;
        }
        boolean success = userController.login(userId, password);
        if (success) {
            dispose();
            userController.showMainView();
        }
        else {
            JOptionPane.showMessageDialog(this, "Tài khoản hoặc mật khẩu không đúng.", "LỖI", JOptionPane.ERROR_MESSAGE);
        }
    }
}