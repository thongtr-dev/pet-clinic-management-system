package com.petclinic.controller;

import com.petclinic.dao.PetDAO;
import com.petclinic.model.Pet;
import com.petclinic.util.SessionManager;
import com.petclinic.view.PetView;
import java.sql.SQLException;
import java.util.List;
import javax.swing.JOptionPane;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

public class PetController {
    private PetView petView;
    private PetDAO petDAO;
    private SessionManager sessionManager;
    private NavigationController navigationController;

    public PetController(PetView petView, PetDAO petDAO) {
        this.petView = petView;
        this.petDAO = petDAO;
        this.sessionManager = SessionManager.getInstance();
        attachEventListeners();
        refreshPetsTable();
    }
    
    public void setNavigationController(NavigationController navigationController) { this.navigationController = navigationController; }

    private void attachEventListeners() {
        petView.getBackButton().addActionListener(e -> {
            if (navigationController != null) {
                navigationController.showUserView();
            }
        });
        petView.getAddButton().addActionListener(e -> petView.showAdd());
        petView.getUpdateButton().addActionListener(e -> {
            int selectedPetId = petView.getSelectedPet();
            if (selectedPetId == -1) {
                petView.showMessage("Hãy chọn hồ sơ thú cưng cần cập nhật!");
                return;
            }
            try {
                Pet pet = petDAO.getById(selectedPetId);
                if (pet != null) {
                    petView.showUpdate(pet);
                }
            }
            catch (SQLException ex) {
                petView.showMessage("Database error: " + ex.getMessage());
            }
        });
        petView.getDeleteButton().addActionListener(e -> handleDelete());
        petView.getRefreshButton().addActionListener(e -> refreshPetsTable());
        petView.getConfirmButton().addActionListener(e -> {
            if (petView.isAddMode()) {
                handleAdd();
            }
            else {
                handleUpdate();
            }
        });
        petView.getCancelButton().addActionListener(e -> petView.hideForm());
        petView.getPetTable().getSelectionModel().addListSelectionListener(new ListSelectionListener() {
            @Override
            public void valueChanged(ListSelectionEvent e) {
                if (!e.getValueIsAdjusting()) {}
            }
        });
    }

    private void refreshPetsTable() {
        try {
            List<Pet> pets = petDAO.getAll();
            petView.showPets(pets);
        }
        catch (SQLException ex) {
            petView.showMessage("Database error: " + ex.getMessage());
        }
    }

    private void handleAdd() {
        Pet pet = petView.getPetFields();
        if (pet.getName().trim().isEmpty()) {
            petView.showMessage("Hãy nhập tên thú cưng!");
            return;
        }
        try {
            boolean success = petDAO.add(pet);
            if (success) {
                petView.showMessage("Thêm hồ sơ thú cưng thành công!");
                petView.hideForm();
                refreshPetsTable();
            }
            else {
                petView.showMessage("Thêm hồ sơ thú cưng thất bại!");
            }
        }
        catch (SQLException ex) {
            petView.showMessage("Database error: " + ex.getMessage());
        }
    }

    private void handleUpdate() {
        Pet pet = petView.getPetFields();
        if (pet.getId() == 0) {
            petView.showMessage("Hãy chọn hồ sơ thú cưng cần cập nhật!");
            return;
        }
        if (pet.getName().trim().isEmpty()) {
            petView.showMessage("Hãy nhập tên thú cưng!");
            return;
        }
        try {
            boolean success = petDAO.update(pet);
            if (success) {
                petView.showMessage("Cập nhật hồ sơ thú cưng thành công!");
                petView.hideForm();
                refreshPetsTable();
            }
            else {
                petView.showMessage("Cập nhật hồ sơ thú cưng thất bại!");
            }
        }
        catch (SQLException ex) {
            petView.showMessage("Database error: " + ex.getMessage());
        }
    }

    private void handleDelete() {
        int petId = petView.getSelectedPet();
        if (petId == -1) {
            petView.showMessage("Hãy chọn hồ sơ thú cưng để xóa!");
            return;
        }
        int confirm = JOptionPane.showConfirmDialog(petView, "Bạn có chắc chắn muốn xóa hồ sơ thú cưng này không?", "Thông báo", JOptionPane.YES_NO_OPTION);
        if (confirm == JOptionPane.YES_OPTION) {
            try {
                boolean success = petDAO.delete(petId);
                if (success) {
                    petView.showMessage("Xóa hồ sơ thú cưng thành công!");
                    refreshPetsTable();
                }
                else {
                    petView.showMessage("Xóa hồ sơ thú cưng thất bại!");
                }
            }
            catch (SQLException ex) {
                petView.showMessage("Database error: " + ex.getMessage());
            }
        }
    }
}