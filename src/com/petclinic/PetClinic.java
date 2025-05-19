package com.petclinic;

import com.petclinic.controller.UserController;
import com.petclinic.dao.UserDAO;
import com.petclinic.dao.UserDAOImpl;
import com.petclinic.view.UserView;
import javax.swing.*;

public class PetClinic {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            UserView userView = new UserView();
            UserDAO userDAO = new UserDAOImpl();
            UserController userController = new UserController(userView, userDAO);
            userView.setVisible(true);
        });
    }
}