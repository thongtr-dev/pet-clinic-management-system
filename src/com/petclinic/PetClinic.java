package com.petclinic;

import com.petclinic.controller.UserController;
import com.petclinic.dao.UserDAO;
import com.petclinic.dao.UserDAOImpl;
import com.petclinic.view.UserView;
import com.petclinic.controller.PetController;
import com.petclinic.dao.PetDAO;
import com.petclinic.dao.PetDAOImpl;
import com.petclinic.view.PetView;
import com.petclinic.controller.NavigationController;
import com.petclinic.view.MainFrame;
import javax.swing.SwingUtilities;

public class PetClinic {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            final MainFrame mainFrame = new MainFrame();
            final NavigationController navigationController = new NavigationController(mainFrame);
            final UserView userView = new UserView();
            final UserDAO userDAO = new UserDAOImpl();
            final PetView petView = new PetView();
            final PetDAO petDAO = new PetDAOImpl();
            new UserController(userView, userDAO, navigationController);
            new PetController(petView, petDAO, navigationController);
            mainFrame.addView(userView, "USER_VIEW");
            mainFrame.addView(petView, "PET_VIEW");
            navigationController.navigateTo("USER_VIEW");
            mainFrame.setVisible(true);
        });
    }
}