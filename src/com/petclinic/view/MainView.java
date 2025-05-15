package com.petclinic.view;

import com.petclinic.controller.UserController;
import com.petclinic.util.SessionManager;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;

public class MainView extends JFrame {
    private UserController userController;
    private JButton btnFeatureA;
    private JButton btnFeatureB;
    private JButton btnFeatureC;
    private JButton btnFeatureD;
    private JButton btnFeatureE;
    private JButton btnLogout;
    private JLabel lblWelcome;
    private JLabel lblTitle;

    public MainView(UserController userController) {
        this.userController = userController;
        initComponents();
    }

    private void initComponents() {
        setTitle("Hệ Thống Quản Lý Phòng Khám Thú Cưng");
        setSize(600, 400);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        // Bảng Điều Khiển Panel
        JPanel mainPanel = new JPanel(new BorderLayout(10, 10));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        JPanel topPanel = new JPanel(new BorderLayout());
        lblTitle = new JLabel("Bảng Điều Khiển", SwingConstants.CENTER);
        lblTitle.setFont(new Font("Arial", Font.BOLD, 18));
        String userName = SessionManager.getInstance().getCurrentUser().getName();
        lblWelcome = new JLabel("Xin chào, " + userName + "!", SwingConstants.RIGHT);
        lblWelcome.setFont(new Font("Arial", Font.ITALIC, 14));
        topPanel.add(lblTitle, BorderLayout.CENTER);
        topPanel.add(lblWelcome, BorderLayout.SOUTH);
        mainPanel.add(topPanel, BorderLayout.NORTH);
        JPanel buttonPanel = new JPanel(new GridLayout(5, 1, 0, 10));
        buttonPanel.setBorder(BorderFactory.createEmptyBorder(10, 50, 10, 50));
        // Nút Chức Năng
        btnFeatureA = new JButton("Quản Lý Hồ Sơ Thú Cưng");
        btnFeatureA.addActionListener((ActionEvent e) -> { });
        btnFeatureA.setPreferredSize(new Dimension(120, 40));
        btnFeatureB = new JButton("Quản Lý Hồ Sơ Chủ Nuôi");
        btnFeatureB.addActionListener((ActionEvent e) -> { });
        btnFeatureB.setPreferredSize(new Dimension(120, 40));
        btnFeatureC = new JButton("Lịch Hẹn");
        btnFeatureC.addActionListener((ActionEvent e) -> { });
        btnFeatureC.setPreferredSize(new Dimension(120, 40));
        btnFeatureD = new JButton("Tìm Kiếm & Lọc");
        btnFeatureD.addActionListener((ActionEvent e) -> { });
        btnFeatureD.setPreferredSize(new Dimension(120, 40));
        btnFeatureE = new JButton("Hồ Sơ Y Tế");
        btnFeatureE.addActionListener((ActionEvent e) -> { });
        btnFeatureE.setPreferredSize(new Dimension(120, 40));
        buttonPanel.add(btnFeatureA);
        buttonPanel.add(btnFeatureB);
        buttonPanel.add(btnFeatureC);
        buttonPanel.add(btnFeatureD);
        buttonPanel.add(btnFeatureE);
        mainPanel.add(buttonPanel, BorderLayout.CENTER);
        JPanel bottomPanel = new JPanel(new BorderLayout());
        JPanel logoutPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        // Nút Đăng Xuất
        btnLogout = new JButton("Đăng Xuất");
        btnLogout.setFont(new Font("Arial", Font.PLAIN, 12));
        btnLogout.addActionListener((ActionEvent e) -> {
            logout();
        });
        logoutPanel.add(btnLogout);
        bottomPanel.add(logoutPanel, BorderLayout.NORTH);
        // Thông Tin Panel
        JPanel infoPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        JLabel lblInfo = new JLabel("© 2025 - Hệ Thống Quản Lý Phòng Khám Thú Cưng");
        lblInfo.setFont(new Font("Arial", Font.PLAIN, 12));
        infoPanel.add(lblInfo);
        bottomPanel.add(infoPanel, BorderLayout.SOUTH);
        mainPanel.add(bottomPanel, BorderLayout.SOUTH);
        add(mainPanel);
    }

    private void logout() {
        int option = JOptionPane.showConfirmDialog(this, "Bạn có chắc chắn muốn đăng xuất?", "XÁC NHẬN", JOptionPane.YES_NO_OPTION);
        if (option == JOptionPane.YES_OPTION) {
            dispose();
            userController.logout();
        }
    }
}