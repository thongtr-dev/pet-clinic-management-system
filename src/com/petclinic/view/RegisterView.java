package com.petclinic.view;

import com.petclinic.controller.UserController;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;

public class RegisterView extends JFrame {
    private UserController userController;
    private JTextField txtName;
    private JTextField txtEmail;
    private JPasswordField txtPassword;
    private JButton btnRegister;
    private JButton btnBackToLogin;
    private JLabel lblTitle;

    public RegisterView(UserController userController) {
        this.userController = userController;
        initComponents();
    }

    private void initComponents() {
        setTitle("Hệ Thống Quản Lý Phòng Khám Thú Cưng");
        setSize(450, 350);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setResizable(false);
        // Đăng Ký Panel
        JPanel mainPanel = new JPanel(new BorderLayout(10, 10));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        lblTitle = new JLabel("Đăng Ký Tài Khoản", SwingConstants.CENTER);
        lblTitle.setFont(new Font("Arial", Font.BOLD, 18));
        mainPanel.add(lblTitle, BorderLayout.NORTH);
        JPanel inputPanel = new JPanel(new GridLayout(3, 2, 10, 10));
        JLabel lblName = new JLabel("Tên Đầy Đủ:");
        txtName = new JTextField(20);
        JLabel lblEmail = new JLabel("Email:");
        txtEmail = new JTextField(20);
        JLabel lblPassword = new JLabel("Mật khẩu:");
        txtPassword = new JPasswordField(20);
        inputPanel.add(lblName);
        inputPanel.add(txtName);
        inputPanel.add(lblEmail);
        inputPanel.add(txtEmail);
        inputPanel.add(lblPassword);
        inputPanel.add(txtPassword);
        // Nút Đăng Ký
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 10));
        btnRegister = new JButton("Đăng Ký");
        btnRegister.addActionListener((ActionEvent e) -> {
            register();
        });
        btnRegister.setPreferredSize(new Dimension(120, 40));
        // Nút Quay Lại
        btnBackToLogin = new JButton("Quay Lại");
        btnBackToLogin.addActionListener((ActionEvent e) -> {
            dispose();
            LoginView loginView = new LoginView(userController);
            loginView.setVisible(true);
        });
        btnBackToLogin.setPreferredSize(new Dimension(120, 40));
        buttonPanel.add(btnRegister);
        buttonPanel.add(btnBackToLogin);
        JPanel centerPanel = new JPanel(new BorderLayout(0, 20));
        centerPanel.add(inputPanel, BorderLayout.CENTER);
        centerPanel.add(buttonPanel, BorderLayout.SOUTH);
        mainPanel.add(centerPanel, BorderLayout.CENTER);
        add(mainPanel);
    }

    private void register() {
        String name = txtName.getText().trim();
        String email = txtEmail.getText().trim();
        String password = new String(txtPassword.getPassword());
        if (name.isEmpty() || email.isEmpty() || password.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Vui lòng nhập đầy đủ thông tin đăng ký.", "THÔNG BÁO", JOptionPane.WARNING_MESSAGE);
            return;
        }
        if (!email.contains("@") || !email.contains(".")) {
            JOptionPane.showMessageDialog(this, "Email không đúng định dạng.", "LỖI", JOptionPane.ERROR_MESSAGE);
            return;
        }
        String userId = userController.register(name, email, password);
        if (userId != null) {
            JOptionPane.showMessageDialog(this, "Đăng ký thành công! Bạn có thể đăng nhập bằng tài khoản " + userId + ".", "THÔNG BÁO", JOptionPane.INFORMATION_MESSAGE);
            dispose();
            LoginView loginView = new LoginView(userController);
            loginView.setVisible(true);
        }
    }
}