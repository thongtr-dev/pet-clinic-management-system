package com.petclinic.controller;

import com.petclinic.view.MainFrame;

public class NavigationController {
    private final MainFrame mainFrame;

    public NavigationController(MainFrame mainFrame) {
        this.mainFrame = mainFrame;
    }

    public void navigateTo(String viewName) {
        mainFrame.showView(viewName);
    }
}