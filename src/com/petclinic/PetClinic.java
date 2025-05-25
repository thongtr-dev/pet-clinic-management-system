package com.petclinic;

import com.petclinic.controller.AppointmentController;
import com.petclinic.controller.MedicalRecordController;
import com.petclinic.controller.NavigationController;
import com.petclinic.controller.OwnerController;
import com.petclinic.controller.PetController;
import com.petclinic.controller.SearchController;
import com.petclinic.controller.UserController;
import com.petclinic.dao.AppointmentDAO;
import com.petclinic.dao.AppointmentDAOImpl;
import com.petclinic.dao.MedicalRecordDAO;
import com.petclinic.dao.MedicalRecordDAOImpl;
import com.petclinic.dao.OwnerDAO;
import com.petclinic.dao.OwnerDAOImpl;
import com.petclinic.dao.PetDAO;
import com.petclinic.dao.PetDAOImpl;
import com.petclinic.dao.UserDAO;
import com.petclinic.dao.UserDAOImpl;
import com.petclinic.view.AppointmentView;
import com.petclinic.view.MainFrame;
import com.petclinic.view.MedicalRecordView;
import com.petclinic.view.OwnerView;
import com.petclinic.view.PetView;
import com.petclinic.view.SearchView;
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
            final AppointmentView appointmentView = new AppointmentView();
            final AppointmentDAO appointmentDAO = new AppointmentDAOImpl();
            final MedicalRecordView medicalRecordView = new MedicalRecordView();
            final MedicalRecordDAO medicalRecordDAO = new MedicalRecordDAOImpl();
            final SearchView searchView = new SearchView();
            final SearchController searchController = new SearchController(searchView, petDAO, appointmentDAO, navigationController);

            ownerView.setupPetViewCallback(navigationController, petView);
            petView.setupAppointmentViewCallback(navigationController, appointmentView, petDAO);
            petView.setupMedicalRecordViewCallback(navigationController, medicalRecordView, petDAO);

            new UserController(userView, userDAO, navigationController);
            new PetController(petView, petDAO, navigationController);
            new OwnerController(ownerView, ownerDAO, navigationController);
            new AppointmentController(appointmentView, appointmentDAO, petDAO, navigationController);
            new MedicalRecordController(medicalRecordView, medicalRecordDAO, petDAO, navigationController);
            mainFrame.addView(userView, "USER_VIEW");
            mainFrame.addView(petView, "PET_VIEW");
            mainFrame.addView(ownerView, "OWNER_VIEW");
            mainFrame.addView(appointmentView, "APPOINTMENT_VIEW");
            mainFrame.addView(medicalRecordView, "MEDICAL_RECORD_VIEW");
            mainFrame.addView(searchView, "SEARCH_VIEW");
            navigationController.navigateTo("USER_VIEW");
            mainFrame.setVisible(true);
        });
    }
}