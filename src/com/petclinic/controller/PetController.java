package com.petclinic.controller;

import com.petclinic.dao.PetDAO;
import com.petclinic.model.Pet;
import com.petclinic.util.SessionManager;
import com.petclinic.view.PetView;
import java.sql.SQLException;
import java.util.List;
import javax.swing.JOptionPane;

public class PetController {
    private final PetView petView;
    private final PetDAO petDAO;
    private final SessionManager sessionManager;
    private final NavigationController navigationController;

    public PetController(PetView petView, PetDAO petDAO, NavigationController navigationController) {
        this.petView = petView;
        this.petDAO = petDAO;
        this.sessionManager = SessionManager.getInstance();
        this.navigationController = navigationController;
        attachEventListeners();
        refreshPetsTable();
    }

    private void attachEventListeners() {
        petView.getBackButton().addActionListener(e -> showDashboard());
        petView.getAddButton().addActionListener(e -> showAddForm());
        petView.getUpdateButton().addActionListener(e -> handleUpdateAction());
        petView.getDeleteButton().addActionListener(e -> handleDelete());
        petView.getRefreshButton().addActionListener(e -> refreshPetsTable());
        petView.getConfirmButton().addActionListener(e -> handleFormSubmit());
        petView.getCancelButton().addActionListener(e -> petView.hideForm());
        petView.getPetTable().getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                boolean hasSelection = petView.getSelectedPet() != -1;
                petView.getUpdateButton().setEnabled(hasSelection);
                petView.getDeleteButton().setEnabled(hasSelection);
            }
        });
    }

    private void showDashboard() {
        if (navigationController != null) {
            navigationController.navigateTo("USER_VIEW");
        }
    }

    private void showAddForm() {
        if (!sessionManager.isLoggedIn()) {
            JOptionPane.showMessageDialog(petView, "Hãy đăng nhập tài khoản trước!");
            navigationController.navigateTo("USER_VIEW");
            return;
        }
        petView.showAddForm();
    }

    private void handleAdd() {
        Pet pet = petView.getPetFields();
        if (validate(pet)) {
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
    }

    private void handleUpdateAction() {
        if (!sessionManager.isLoggedIn()) {
            JOptionPane.showMessageDialog(petView, "Phiên đăng nhập đã hết hạn!");
            navigationController.navigateTo("USER_VIEW");
            return;
        }
        try {
            int selectedPetId = petView.getSelectedPet();
            if (selectedPetId == -1) {
                return;
            }
            Pet pet = petDAO.getById(selectedPetId);
            if (pet != null) {
                petView.showUpdateForm(pet);
            }
        }
        catch (SQLException ex) {
            petView.showMessage("Database error: " + ex.getMessage());
        }
    }

    private void handleUpdate() {
        Pet pet = petView.getPetFields();
        if (validate(pet)) {
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
    }

    private void handleDelete() {
        if (!sessionManager.isLoggedIn()) {
            JOptionPane.showMessageDialog(petView, "Không được phép xóa hồ sơ thú cưng!");
            return;
        }
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

    private void refreshPetsTable() {
        try {
            List<Pet> pets = petDAO.getAll();
            petView.showPets(pets);
            petView.getUpdateButton().setEnabled(false);
            petView.getDeleteButton().setEnabled(false);
        }
        catch (SQLException ex) {
            petView.showMessage("Database error: " + ex.getMessage());
        }
    }

    private void handleFormSubmit() {
        if (petView.isAddMode()) {
            handleAdd();
        }
        else {
            handleUpdate();
        }
    }

    private boolean validate(Pet pet) {
        if (pet.getName().trim().isEmpty()) {
            petView.showMessage("Hãy nhập tên thú cưng!");
            return false;
        }
        if (pet.getAge() <= 0) {
            petView.showMessage("Tuổi thú cưng phải là số dương!");
            return false;
        }
        return true;
    }
}