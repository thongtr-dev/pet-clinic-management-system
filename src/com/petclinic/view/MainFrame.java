package com.petclinic.view;

import java.awt.*;
import javax.swing.*;

public class MainFrame extends JFrame {
    private final CardLayout cardLayout;
    private final JPanel mainPanel;

    public MainFrame() {
        setTitle("HỆ THỐNG QUẢN LÝ PHÒNG KHÁM THÚ CƯNG");
        setSize(800, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        cardLayout = new CardLayout();
        mainPanel = new JPanel(cardLayout);
        add(mainPanel);
    }

    public void addView(JPanel view, String name) { mainPanel.add(view, name); }

    public void showView(String name) { cardLayout.show(mainPanel, name); }
}