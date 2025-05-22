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
import com.petclinic.view.MainFrame;
import com.petclinic.view.OwnerView;
import com.petclinic.view.PetView;
import com.petclinic.view.UserView;
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
            final OwnerView ownerView = new OwnerView();
            final OwnerDAO ownerDAO = new OwnerDAOImpl();

            ownerView.setPetViewCallback(new OwnerView.PetViewCallback() {
                @Override
                public void openPetAddForm(int ownerId) {
                    navigationController.navigateTo("PET_VIEW");
                    petView.showAddForm(ownerId);
                }
            });

            new UserController(userView, userDAO, navigationController);
            new PetController(petView, petDAO, navigationController);
            new OwnerController(ownerView, ownerDAO, navigationController);
            mainFrame.addView(userView, "USER_VIEW");
            mainFrame.addView(petView, "PET_VIEW");
            mainFrame.addView(ownerView, "OWNER_VIEW");
            navigationController.navigateTo("USER_VIEW");
            mainFrame.setVisible(true);
        });
    }
}