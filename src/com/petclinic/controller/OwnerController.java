package com.petclinic.controller;

import com.petclinic.dao.OwnerDAO;
import com.petclinic.model.Owner;
import com.petclinic.util.SessionManager;
import com.petclinic.view.OwnerView;
import com.petclinic.view.UserView;
import java.sql.SQLException;
import java.util.List;
import javax.swing.JOptionPane;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

public class OwnerController {
    private UserView userView;
    private OwnerView ownerView;
    private OwnerDAO ownerDAO;
    private SessionManager sessionManager;

    public OwnerController(OwnerView ownerView, OwnerDAO ownerDAO, UserView userView) {
        this.ownerView = ownerView;
        this.ownerDAO = ownerDAO;
        this.userView = userView;
        this.sessionManager = SessionManager.getInstance();
        attachEventListeners();
        refreshOwnersTable();
    }

    private void attachEventListeners() {
        ownerView.getBackButton().addActionListener(e -> {
            ownerView.setVisible(false);
            userView.setVisible(true);
        });
        ownerView.getAddButton().addActionListener(e -> ownerView.showAdd());
        ownerView.getUpdateButton().addActionListener(e -> {
            int selectedOwnerId = ownerView.getSelectedOwner();
            if (selectedOwnerId == -1) {
                ownerView.showMessage("Hãy chọn hồ sơ chủ nuôi cần cập nhật!");
                return;
            }
            try {
                Owner owner = ownerDAO.getById(selectedOwnerId);
                if (owner != null) {
                    ownerView.showUpdate(owner);
                }
            }
            catch (SQLException ex) {
                ownerView.showMessage("Database error: " + ex.getMessage());
            }
        });
        ownerView.getDeleteButton().addActionListener(e -> handleDelete());
        ownerView.getRefreshButton().addActionListener(e -> {
            refreshOwnersTable();
        });
        ownerView.getConfirmButton().addActionListener(e -> {
            if (ownerView.isAddMode()) {
                handleAdd();
            }
            else {
                handleUpdate();
            }
        });
        ownerView.getCancelButton().addActionListener(e -> ownerView.hideForm());
        ownerView.getOwnerTable().getSelectionModel().addListSelectionListener(new ListSelectionListener() {
            @Override
            public void valueChanged(ListSelectionEvent e) {
                if (!e.getValueIsAdjusting()) {}
            }
        });
    }

    private void refreshOwnersTable() {
        try {
            List<Owner> owners = ownerDAO.getAll();
            ownerView.showOwners(owners);
        }
        catch (SQLException ex) {
            ownerView.showMessage("Database error: " + ex.getMessage());
        }
    }

    private void handleAdd() {
        Owner owner = ownerView.getOwnerFields();
        if (owner.getName().trim().isEmpty()) {
            ownerView.showMessage("Hãy nhập tên chủ nuôi!");
            return;
        }
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

    private void handleUpdate() {
        Owner owner = ownerView.getOwnerFields();
        if (owner.getId() == 0) {
            ownerView.showMessage("Hãy chọn hồ sơ chủ nuôi cần cập nhật!");
            return;
        }
        if (owner.getName().trim().isEmpty()) {
            ownerView.showMessage("Hãy nhập tên chủ nuôi!");
            return;
        }
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

    private void handleDelete() {
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
}