package com.petclinic.controller;

import com.petclinic.dao.AppointmentDAO;
import com.petclinic.dao.PetDAO;
import com.petclinic.model.Appointment;
import com.petclinic.util.SessionManager;
import com.petclinic.view.AppointmentView;

import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.List;
import javax.swing.JOptionPane;

public class AppointmentController {
    private final AppointmentView appointmentView;
    private final AppointmentDAO appointmentDAO;
    private final PetDAO petDAO;
    private final SessionManager sessionManager;
    private final NavigationController navigationController;

    public AppointmentController(AppointmentView appointmentView,
                                 AppointmentDAO appointmentDAO,
                                 PetDAO petDAO,
                                 NavigationController navigationController) {
        this.appointmentView = appointmentView;
        this.appointmentDAO = appointmentDAO;
        this.petDAO = petDAO;
        this.sessionManager = SessionManager.getInstance();
        this.navigationController = navigationController;
        attachEventListeners();
        refreshAppointmentsTable();
    }

    private void attachEventListeners() {
        appointmentView.getBackButton().addActionListener(e -> showDashboard());
        appointmentView.getAddButton().addActionListener(e -> showAddForm());
        appointmentView.getUpdateButton().addActionListener(e -> handleUpdateAction());
        appointmentView.getDeleteButton().addActionListener(e -> handleDelete());
        appointmentView.getRefreshButton().addActionListener(e -> refreshAppointmentsTable());
        appointmentView.getConfirmButton().addActionListener(e -> handleFormSubmit());
        appointmentView.getCancelButton().addActionListener(e -> appointmentView.hideForm());
        appointmentView.getAppointmentTable().getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                boolean hasSelection = appointmentView.getSelectedAppointment() != -1;
                appointmentView.getUpdateButton().setEnabled(hasSelection);
                appointmentView.getDeleteButton().setEnabled(hasSelection);
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
            JOptionPane.showMessageDialog(appointmentView, "Hãy đăng nhập tài khoản trước!");
            navigationController.navigateTo("USER_VIEW");
            return;
        }
        appointmentView.showAddForm();
    }

    private void handleAdd() {
        Appointment appointment = appointmentView.getAppointmentFields();
        if (validate(appointment)) {
            try {
                boolean success = appointmentDAO.add(appointment);
                if (success) {
                    appointmentView.showMessage("Thêm lịch hẹn thành công!");
                    appointmentView.hideForm();
                    refreshAppointmentsTable();
                } else {
                    appointmentView.showMessage("Thêm lịch hẹn thất bại!");
                }
            } catch (SQLException ex) {
                appointmentView.showMessage("Database error: " + ex.getMessage());
            }
        }
    }

    private void handleUpdateAction() {
        if (!sessionManager.isLoggedIn()) {
            JOptionPane.showMessageDialog(appointmentView, "Hãy đăng nhập tài khoản trước!");
            navigationController.navigateTo("USER_VIEW");
            return;
        }
        try {
            int selectedAppointmentId = appointmentView.getSelectedAppointment();
            Appointment appointment = appointmentDAO.getById(selectedAppointmentId);
            if (appointment != null) {
                appointmentView.showUpdateForm(appointment);
            }
        } catch (SQLException ex) {
            appointmentView.showMessage("Database error: " + ex.getMessage());
        }
    }

    private void handleUpdate() {
        Appointment appointment = appointmentView.getAppointmentFields();
        if (validate(appointment)) {
            try {
                boolean success = appointmentDAO.update(appointment);
                if (success) {
                    appointmentView.showMessage("Cập nhật lịch hẹn thành công!");
                    appointmentView.hideForm();
                    refreshAppointmentsTable();
                } else {
                    appointmentView.showMessage("Cập nhật lịch hẹn thất bại!");
                }
            } catch (SQLException ex) {
                appointmentView.showMessage("Database error: " + ex.getMessage());
            }
        }
    }

    private void handleDelete() {
        if (!sessionManager.isLoggedIn()) {
            JOptionPane.showMessageDialog(appointmentView, "Hãy đăng nhập tài khoản trước!");
            navigationController.navigateTo("USER_VIEW");
            return;
        }
        int appointmentId = appointmentView.getSelectedAppointment();

        int confirm = JOptionPane.showConfirmDialog(appointmentView,
                "Bạn có chắc chắn muốn xóa lịch hẹn này không?", "Thông báo", JOptionPane.YES_NO_OPTION);
        if (confirm == JOptionPane.YES_OPTION) {
            try {
                boolean success = appointmentDAO.delete(appointmentId);
                if (success) {
                    appointmentView.showMessage("Xóa lịch hẹn thành công!");
                    refreshAppointmentsTable();
                } else {
                    appointmentView.showMessage("Xóa lịch hẹn thất bại!");
                }
            } catch (SQLException ex) {
                appointmentView.showMessage("Database error: " + ex.getMessage());
            }
        }
    }

    private void refreshAppointmentsTable() {
        try {
            List<Appointment> appointments = appointmentDAO.getAll();
            appointmentView.showAppointments(appointments);
            appointmentView.getUpdateButton().setEnabled(false);
            appointmentView.getDeleteButton().setEnabled(false);
        } catch (SQLException ex) {
            appointmentView.showMessage("Database error: " + ex.getMessage());
        }
    }

    private void handleFormSubmit() {
        if (appointmentView.isAddMode()) {
            handleAdd();
        } else {
            handleUpdate();
        }
    }

    private boolean validate(Appointment appointment) {
        if (appointment.getAppointmentDate() == null) {
            appointmentView.showMessage("Hãy nhập ngày hẹn!");
            return false;
        }
        if (appointment.getAppointmentDate().isBefore(LocalDateTime.now())) {
            appointmentView.showMessage("Ngày hẹn phải là thời gian trong tương lai!");
            return false;
        }
        return true;
    }
}