package com.petclinic;

import com.petclinic.controller.NavigationController;
import com.petclinic.controller.OwnerController;
import com.petclinic.controller.PetController;
import com.petclinic.controller.UserController;
import com.petclinic.dao.OwnerDAO;
import com.petclinic.dao.OwnerDAOImpl;
import com.petclinic.dao.PetDAO;
import com.petclinic.dao.PetDAOImpl;
import com.petclinic.dao.UserDAO;
import com.petclinic.dao.UserDAOImpl;
import com.petclinic.view.OwnerView;
import com.petclinic.view.PetView;
import com.petclinic.view.UserView;
import javax.swing.*;

public class PetClinic {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            UserView userView = new UserView();
            UserDAO userDAO = new UserDAOImpl();
            UserController userController = new UserController(userView, userDAO);
            PetView petView = new PetView();
            PetDAO petDAO = new PetDAOImpl();
            PetController petController = new PetController(petView, petDAO);
            OwnerView ownerView = new OwnerView();
            OwnerDAO ownerDAO = new OwnerDAOImpl();
            OwnerController ownerController = new OwnerController(ownerView, ownerDAO);
            NavigationController navigationController = new NavigationController(userView, petView, ownerView);
            userController.setNavigationController(navigationController);
            petController.setNavigationController(navigationController);
            ownerController.setNavigationController(navigationController);
            userView.setVisible(true);
        });
    }
}