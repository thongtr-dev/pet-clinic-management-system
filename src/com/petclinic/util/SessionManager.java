package com.petclinic.util;

import com.petclinic.model.User;

public class SessionManager {
    private static SessionManager instance;
    private User currentUser;
    private boolean loggedIn = false;

    private SessionManager() {}

    public static SessionManager getInstance() {
        if (instance == null) {
            instance = new SessionManager();
        }
        return instance;
    }

    public void login(User user) {
        this.currentUser = user;
        this.loggedIn = true;
    }

    public void logout() {
        this.currentUser = null;
        this.loggedIn = false;
    }

    public User getCurrentUser() { return currentUser; }

    public boolean isLoggedIn() { return loggedIn; }
}