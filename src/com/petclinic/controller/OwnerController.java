package com.petclinic.controller;

import com.petclinic.dao.OwnerDAO;
import com.petclinic.model.Owner;
import com.petclinic.util.SessionManager;
import com.petclinic.view.OwnerView;
import java.sql.SQLException;
import java.util.List;
import javax.swing.JOptionPane;

public class OwnerController {
    private final OwnerView ownerView;
    private final OwnerDAO ownerDAO;
    private final SessionManager sessionManager;
    private final NavigationController navigationController;

    public OwnerController(OwnerView ownerView, OwnerDAO ownerDAO, NavigationController navigationController) {
        this.ownerView = ownerView;
        this.ownerDAO = ownerDAO;
        this.sessionManager = SessionManager.getInstance();
        this.navigationController = navigationController;
        attachEventListeners();
        refreshOwnersTable();
    }

    private void attachEventListeners() {
        ownerView.getBackButton().addActionListener(e -> showDashboard());
        ownerView.getAddButton().addActionListener(e -> showAddForm());
        ownerView.getUpdateButton().addActionListener(e -> handleUpdateAction());
        ownerView.getDeleteButton().addActionListener(e -> handleDelete());
        ownerView.getRefreshButton().addActionListener(e -> refreshOwnersTable());
        ownerView.getConfirmButton().addActionListener(e -> handleFormSubmit());
        ownerView.getCancelButton().addActionListener(e -> ownerView.hideForm());
        ownerView.getOwnerTable().getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                boolean hasSelection = ownerView.getSelectedOwner() != -1;
                ownerView.getUpdateButton().setEnabled(hasSelection);
                ownerView.getDeleteButton().setEnabled(hasSelection);
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
            JOptionPane.showMessageDialog(ownerView, "Hãy đăng nhập tài khoản trước!");
            navigationController.navigateTo("USER_VIEW");
            return;
        }
        ownerView.showAddForm();
    }

    private void handleAdd() {
        Owner owner = ownerView.getOwnerFields();
        if (validate(owner)) {
            try {
                boolean success = ownerDAO.add(owner);
                if (success) {
                    ownerView.showMessage("Thêm hồ sơ chủ nuôi thành công!");
                    ownerView.hideForm();
                    refreshOwnersTable();
                }
                else {
                    ownerView.showMessage("Thêm hồ sơ chủ nuôi thất bại!");
                }
            }
            catch (SQLException ex) {
                ownerView.showMessage("Database error: " + ex.getMessage());
            }
        }
    }

    private void handleUpdateAction() {
        if (!sessionManager.isLoggedIn()) {
            JOptionPane.showMessageDialog(ownerView, "Phiên đăng nhập đã hết hạn!");
            navigationController.navigateTo("USER_VIEW");
            return;
        }
        try {
            int selectedOwnerId = ownerView.getSelectedOwner();
            if (selectedOwnerId == -1) {
                return;
            }
            Owner owner = ownerDAO.getById(selectedOwnerId);
            if (owner != null) {
                ownerView.showUpdateForm(owner);
            }
        }
        catch (SQLException ex) {
            ownerView.showMessage("Database error: " + ex.getMessage());
        }
    }

    private void handleUpdate() {
        Owner owner = ownerView.getOwnerFields();
        if (validate(owner)) {
            try {
                boolean success = ownerDAO.update(owner);
                if (success) {
                    ownerView.showMessage("Cập nhật hồ sơ chủ nuôi thành công!");
                    ownerView.hideForm();
                    refreshOwnersTable();
                }
                else {
                    ownerView.showMessage("Cập nhật hồ sơ chủ nuôi thất bại!");
                }
            }
            catch (SQLException ex) {
                ownerView.showMessage("Database error: " + ex.getMessage());
            }
        }
    }

    private void handleDelete() {
        if (!sessionManager.isLoggedIn()) {
            JOptionPane.showMessageDialog(ownerView, "Không được phép xóa hồ sơ chủ nuôi!");
            return;
        }
        int ownerId = ownerView.getSelectedOwner();
        if (ownerId == -1) {
            ownerView.showMessage("Hãy chọn hồ sơ chủ nuôi để xóa!");
            return;
        }
        int confirm = JOptionPane.showConfirmDialog(ownerView, "Bạn có chắc chắn muốn xóa hồ sơ chủ nuôi này không?", "Thông báo", JOptionPane.YES_NO_OPTION);
        if (confirm == JOptionPane.YES_OPTION) {
            try {
                boolean success = ownerDAO.delete(ownerId);
                if (success) {
                    ownerView.showMessage("Xóa hồ sơ chủ nuôi thành công!");
                    refreshOwnersTable();
                }
                else {
                    ownerView.showMessage("Xóa hồ sơ chủ nuôi thất bại!");
                }
            }
            catch (SQLException ex) {
                ownerView.showMessage("Database error: " + ex.getMessage());
            }
        }
    }

    private void refreshOwnersTable() {
        try {
            List<Owner> owners = ownerDAO.getAll();
            ownerView.showOwners(owners);
            ownerView.getUpdateButton().setEnabled(false);
            ownerView.getDeleteButton().setEnabled(false);
        }
        catch (SQLException ex) {
            ownerView.showMessage("Database error: " + ex.getMessage());
        }
    }

    private void handleFormSubmit() {
        if (ownerView.isAddMode()) {
            handleAdd();
        }
        else {
            handleUpdate();
        }
    }
    private boolean validate(Owner owner) {
        if (owner.getFullName().trim().isEmpty()) {
            ownerView.showMessage("Hãy nhập Họ và tên chủ nuôi!");
            return false;
        }
        if (owner.getPhone().trim().isEmpty()) {
            ownerView.showMessage("Hãy nhập Số điện thoại!");
            return false;
        }
        if (!owner.getPhone().matches("^\\+?[0-9\\s-]{7,}$")) {
            ownerView.showMessage("Số điện thoại không hợp lệ!");
            return false;
        }
        if (owner.getEmail() != null && !owner.getEmail().trim().isEmpty()) {
            String emailRegex = "^[\\w.-]+@[\\w.-]+\\.[a-zA-Z]{2,}$";
            if (!owner.getEmail().matches(emailRegex)) {
                ownerView.showMessage("Email không hợp lệ!");
                return false;
            }
        }
        return true;
    }
}
