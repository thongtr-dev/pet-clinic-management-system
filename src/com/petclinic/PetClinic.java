package com.petclinic;

import com.petclinic.controller.UserController;
import com.petclinic.view.LoginView;
import javax.swing.*;

public class PetClinic {
    public static void main(String[] args) {
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                UserController userController = new UserController();
                LoginView loginView = new LoginView(userController);
                loginView.setVisible(true);
            }
        });
    }
}