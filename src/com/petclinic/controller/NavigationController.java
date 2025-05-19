
package com.petclinic.controller;

import com.petclinic.util.SessionManager;
import com.petclinic.view.OwnerView;
import com.petclinic.view.PetView;
import com.petclinic.view.UserView;

public class NavigationController {
    private UserView userView;
    private PetView petView;
    private OwnerView ownerView;
    private SessionManager sessionManager;
    
    public NavigationController(UserView userView, PetView petView, OwnerView ownerView) {
        this.userView = userView;
        this.petView = petView;
        this.ownerView = ownerView;
        this.sessionManager = SessionManager.getInstance();
    }
    
    public void showUserView() {
        userView.setVisible(true);
        petView.setVisible(false);
        ownerView.setVisible(false);
    }
    
    public void showPetView() {
        if (sessionManager.isLoggedIn()) {
            petView.setVisible(true);
            userView.setVisible(false);
            ownerView.setVisible(false);
        }
    }
    public void showOwnerView() {
        if (sessionManager.isLoggedIn()) {
            userView.setVisible(false);
            petView.setVisible(false);
            ownerView.setVisible(true);
        }
    }
}