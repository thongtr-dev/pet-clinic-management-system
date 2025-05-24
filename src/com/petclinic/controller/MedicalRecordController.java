package com.petclinic.controller;

import com.petclinic.dao.MedicalRecordDAO;
import com.petclinic.dao.PetDAO;
import com.petclinic.model.MedicalRecord;
import com.petclinic.model.Pet;
import com.petclinic.util.SessionManager;
import com.petclinic.view.MedicalRecordView;

import java.sql.SQLException;
import java.util.List;
import javax.swing.JOptionPane;

public class MedicalRecordController {
    private final MedicalRecordView medicalRecordView;
    private final MedicalRecordDAO medicalRecordDAO;
    private final PetDAO petDAO;
    private final SessionManager sessionManager;
    private final NavigationController navigationController;

    public MedicalRecordController(MedicalRecordView medicalRecordView, MedicalRecordDAO medicalRecordDAO,
                                   PetDAO petDAO, NavigationController navigationController) {
        this.medicalRecordView = medicalRecordView;
        this.medicalRecordDAO = medicalRecordDAO;
        this.petDAO = petDAO;
        this.sessionManager = SessionManager.getInstance();
        this.navigationController = navigationController;
        attachEventListeners();
        refreshMedicalRecordsTable();
    }

    private void attachEventListeners() {
        medicalRecordView.getBackButton().addActionListener(e -> showDashboard());
        medicalRecordView.getAddButton().addActionListener(e -> showAddForm());
        medicalRecordView.getUpdateButton().addActionListener(e -> handleUpdateAction());
        medicalRecordView.getDeleteButton().addActionListener(e -> handleDelete());
        medicalRecordView.getRefreshButton().addActionListener(e -> refreshMedicalRecordsTable());
        medicalRecordView.getConfirmButton().addActionListener(e -> handleFormSubmit());
        medicalRecordView.getCancelButton().addActionListener(e -> medicalRecordView.hideForm());

        medicalRecordView.getPetIdField().addActionListener(e -> loadPetInfo());

        medicalRecordView.getMedicalRecordTable().getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                boolean hasSelection = medicalRecordView.getSelectedMedicalRecord() != -1;
                medicalRecordView.getUpdateButton().setEnabled(hasSelection);
                medicalRecordView.getDeleteButton().setEnabled(hasSelection);
            }
        });
    }

    private void showDashboard() {
        if (medicalRecordView.isFilterByPet()) {
            medicalRecordView.resetFilterByPet();

            if (navigationController != null) {
                navigationController.navigateTo("PET_VIEW");
                return;
            }
        }

        if (navigationController != null) {
            navigationController.navigateTo("USER_VIEW");
        }
    }

    private void showAddForm() {
        if (!sessionManager.isLoggedIn()) {
            JOptionPane.showMessageDialog(medicalRecordView, "Hãy đăng nhập tài khoản trước!");
            navigationController.navigateTo("USER_VIEW");
            return;
        }
        medicalRecordView.showAddForm();
    }

    private void loadPetInfo() {
        String petIdText = medicalRecordView.getPetIdField().getText();
        if (!petIdText.isEmpty()) {
            try {
                int petId = Integer.parseInt(petIdText);
                Pet pet = petDAO.getById(petId);
                if (pet != null) {
                    medicalRecordView.setMedicalRecordFields(createMedicalRecordFromPet(pet));
                } else {
                    medicalRecordView.showMessage("Không tìm thấy thú cưng với ID: " + petId);
                }
            } catch (NumberFormatException ex) {
                medicalRecordView.showMessage("ID thú cưng phải là số!");
            } catch (SQLException ex) {
                medicalRecordView.showMessage("Database error: " + ex.getMessage());
            }
        }
    }

    private MedicalRecord createMedicalRecordFromPet(Pet pet) {
        MedicalRecord record = new MedicalRecord();
        record.setPetId(pet.getId());
        record.setPetName(pet.getName());
        record.setOwnerName(pet.getOwnerName());
        return record;
    }

    private void handleAdd() {
        MedicalRecord record = medicalRecordView.getMedicalRecordFields();
        if (validate(record)) {
            try {
                boolean success = medicalRecordDAO.add(record);
                if (success) {
                    medicalRecordView.showMessage("Thêm hồ sơ y tế thành công!");
                    medicalRecordView.hideForm();
                    refreshMedicalRecordsTable();
                } else {
                    medicalRecordView.showMessage("Thêm hồ sơ y tế thất bại!");
                }
            } catch (SQLException ex) {
                medicalRecordView.showMessage("Database error: " + ex.getMessage());
            }
        }
    }

    private void handleUpdateAction() {
        if (!sessionManager.isLoggedIn()) {
            JOptionPane.showMessageDialog(medicalRecordView, "Hãy đăng nhập tài khoản trước!");
            navigationController.navigateTo("USER_VIEW");
            return;
        }

        try {
            int selectedRecordId = medicalRecordView.getSelectedMedicalRecord();
            if (selectedRecordId == -1) {
                return;
            }
            MedicalRecord record = medicalRecordDAO.getById(selectedRecordId);
            if (record != null) {
                medicalRecordView.showUpdateForm(record);
            }
        } catch (SQLException ex) {
            medicalRecordView.showMessage("Database error: " + ex.getMessage());
        }
    }

    private void handleUpdate() {
        MedicalRecord record = medicalRecordView.getMedicalRecordFields();
        if (validate(record)) {
            try {
                boolean success = medicalRecordDAO.update(record);
                if (success) {
                    medicalRecordView.showMessage("Cập nhật hồ sơ y tế thành công!");
                    medicalRecordView.hideForm();
                    refreshMedicalRecordsTable();
                } else {
                    medicalRecordView.showMessage("Cập nhật hồ sơ y tế thất bại!");
                }
            } catch (SQLException ex) {
                medicalRecordView.showMessage("Database error: " + ex.getMessage());
            }
        }
    }

    private void handleDelete() {
        if (!sessionManager.isLoggedIn()) {
            JOptionPane.showMessageDialog(medicalRecordView, "Hãy đăng nhập tài khoản trước!");
            navigationController.navigateTo("USER_VIEW");
            return;
        }

        int recordId = medicalRecordView.getSelectedMedicalRecord();
        if (recordId == -1) {
            medicalRecordView.showMessage("Hãy chọn hồ sơ y tế để xóa!");
            return;
        }

        int confirm = JOptionPane.showConfirmDialog(medicalRecordView,
                "Bạn có chắc chắn muốn xóa hồ sơ y tế này không?",
                "Thông báo", JOptionPane.YES_NO_OPTION);

        if (confirm == JOptionPane.YES_OPTION) {
            try {
                boolean success = medicalRecordDAO.delete(recordId);
                if (success) {
                    medicalRecordView.showMessage("Xóa hồ sơ y tế thành công!");
                    refreshMedicalRecordsTable();
                } else {
                    medicalRecordView.showMessage("Xóa hồ sơ y tế thất bại!");
                }
            } catch (SQLException ex) {
                medicalRecordView.showMessage("Database error: " + ex.getMessage());
            }
        }
    }

    private void refreshMedicalRecordsTable() {
        try {
            List<MedicalRecord> records;

            if (medicalRecordView.isFilterByPet() && medicalRecordView.getFilterPetId() > 0) {
                records = medicalRecordDAO.getByPetId(medicalRecordView.getFilterPetId());
            } else {
                records = medicalRecordDAO.getAll();
            }

            medicalRecordView.showMedicalRecords(records);
            medicalRecordView.getUpdateButton().setEnabled(false);
            medicalRecordView.getDeleteButton().setEnabled(false);
        } catch (SQLException ex) {
            medicalRecordView.showMessage("Database error: " + ex.getMessage());
        }
    }

    private void handleFormSubmit() {
        if (medicalRecordView.isAddMode()) {
            handleAdd();
        } else {
            handleUpdate();
        }
    }

    private boolean validate(MedicalRecord record) {
        if (record.getPetId() <= 0) {
            medicalRecordView.showMessage("Hãy nhập mã thú cưng hợp lệ!");
            return false;
        }

        try {
            Pet pet = petDAO.getById(record.getPetId());
            if (pet == null) {
                medicalRecordView.showMessage("Không tìm thấy thú cưng với mã: " + record.getPetId());
                return false;
            }
        } catch (SQLException ex) {
            medicalRecordView.showMessage("Database error: " + ex.getMessage());
            return false;
        }

        if (record.getTreatment() == null || record.getTreatment().trim().isEmpty()) {
            medicalRecordView.showMessage("Hãy nhập thông tin điều trị!");
            return false;
        }

        if (record.getDiagnosis() == null || record.getDiagnosis().trim().isEmpty()) {
            medicalRecordView.showMessage("Hãy nhập thông tin chẩn đoán!");
            return false;
        }

        return true;
    }

    public void prepareForNewMedicalRecord(int petId, String petName, String ownerName) {
        medicalRecordView.showAddForm(petId, petName, ownerName);
    }
}
