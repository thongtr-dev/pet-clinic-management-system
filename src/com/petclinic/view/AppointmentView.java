package com.petclinic.view;

import com.petclinic.model.Appointment;

import java.awt.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;

public class AppointmentView extends JPanel {
    private JPanel mainPanel;
    private JTable appointmentTable;
    private DefaultTableModel tableModel;
    private JTextField idField;
    private JTextField petIdField;
    private JTextField petNameField;
    private JTextField ownerNameField;
    private JTextField dayField;
    private JTextField monthField;
    private JTextField yearField;
    private JComboBox<String> timeComboBox;
    private JComboBox<String> vetComboBox;
    private JTextField notesField;
    private JButton addButton;
    private JButton updateButton;
    private JButton deleteButton;
    private JButton refreshButton;
    private JButton backButton;
    private JButton cancelButton;
    private JButton confirmButton;
    private JLabel welcomeLabel;
    private JPanel formPanel;
    private JDialog formDialog;
    private boolean isAddMode;
    private JLabel idLabel;

    public AppointmentView() {
        initializeMainPanel();
        initializeFormDialog();
        setLayout(new BorderLayout());
        add(mainPanel, BorderLayout.CENTER);
        updateButton.setEnabled(false);
        deleteButton.setEnabled(false);
    }

    private void initializeMainPanel() {
        mainPanel = new JPanel(new BorderLayout());

        JPanel headerPanel = new JPanel(new BorderLayout());
        welcomeLabel = new JLabel("QUẢN LÝ LỊCH HẸN");
        welcomeLabel.setFont(new Font("Arial", Font.BOLD, 18));
        welcomeLabel.setHorizontalAlignment(JLabel.LEFT);
        headerPanel.add(welcomeLabel, BorderLayout.CENTER);
        backButton = new JButton("QUAY LẠI");
        headerPanel.add(backButton, BorderLayout.EAST);
        mainPanel.add(headerPanel, BorderLayout.NORTH);

        String[] columnNames = {"ID", "THÚ CƯNG", "CHỦ NUÔI", "BÁC SĨ", "NGÀY HẸN", "GIỜ HẸN", "GHI CHÚ"};
        tableModel = new DefaultTableModel(columnNames, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        appointmentTable = new JTable(tableModel);
        appointmentTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        JScrollPane tableScrollPane = new JScrollPane(appointmentTable);
        mainPanel.add(tableScrollPane, BorderLayout.CENTER);

        JPanel buttonPanel = new JPanel(new FlowLayout());
        addButton = new JButton("THÊM MỚI");
        updateButton = new JButton("CẬP NHẬT");
        deleteButton = new JButton("XÓA BỎ");
        refreshButton = new JButton("LÀM MỚI");
        buttonPanel.add(addButton);
        addButton.setVisible(false);
        buttonPanel.add(updateButton);
        buttonPanel.add(deleteButton);
        buttonPanel.add(refreshButton);
        mainPanel.add(buttonPanel, BorderLayout.SOUTH);
    }

    private void initializeFormDialog() {
        formDialog = new JDialog();
        formDialog.setTitle("Thông tin lịch hẹn");
        formDialog.setModal(true);
        formDialog.setSize(500, 500);
        formDialog.setLocationRelativeTo(this);

        formPanel = new JPanel(new BorderLayout());
        JPanel inputPanel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(5, 5, 5, 5);

        gbc.gridx = 0;
        gbc.gridy = 0;
        idLabel = new JLabel("ID:");
        inputPanel.add(idLabel, gbc);
        gbc.gridx = 1;
        idField = new JTextField(10);
        idField.setEditable(false);
        inputPanel.add(idField, gbc);

        gbc.gridx = 0;
        gbc.gridy = 1;
        inputPanel.add(new JLabel("ID THÚ CƯNG:"), gbc);
        gbc.gridx = 1;
        petIdField = new JTextField(20);
        inputPanel.add(petIdField, gbc);

        gbc.gridx = 0;
        gbc.gridy = 2;
        inputPanel.add(new JLabel("TÊN THÚ CƯNG:"), gbc);
        gbc.gridx = 1;
        petNameField = new JTextField(20);
        petNameField.setEditable(false);
        petNameField.setBackground(Color.LIGHT_GRAY);
        inputPanel.add(petNameField, gbc);

        gbc.gridx = 0;
        gbc.gridy = 3;
        inputPanel.add(new JLabel("CHỦ NUÔI:"), gbc);
        gbc.gridx = 1;
        ownerNameField = new JTextField(20);
        ownerNameField.setEditable(false);
        ownerNameField.setBackground(Color.LIGHT_GRAY);
        inputPanel.add(ownerNameField, gbc);

        gbc.gridx = 0;
        gbc.gridy = 4;
        inputPanel.add(new JLabel("NGÀY HẸN:"), gbc);

        JPanel datePanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        dayField = new JTextField(3);
        dayField.setToolTipText("DD");
        monthField = new JTextField(3);
        monthField.setToolTipText("MM");
        yearField = new JTextField(5);
        yearField.setToolTipText("YYYY");

        datePanel.add(dayField);
        datePanel.add(new JLabel("/"));
        datePanel.add(monthField);
        datePanel.add(new JLabel("/"));
        datePanel.add(yearField);

        gbc.gridx = 1;
        inputPanel.add(datePanel, gbc);

        gbc.gridx = 0;
        gbc.gridy = 5;
        inputPanel.add(new JLabel("GIỜ HẸN:"), gbc);
        gbc.gridx = 1;

        String[] timeSlots = {
                "8:00 AM", "9:00 AM", "10:00 AM",
                "2:00 PM", "3:00 PM", "4:00 PM", "5:00 PM"
        };
        timeComboBox = new JComboBox<>(timeSlots);
        inputPanel.add(timeComboBox, gbc);

        gbc.gridx = 0;
        gbc.gridy = 6;
        inputPanel.add(new JLabel("BÁC SĨ:"), gbc);
        gbc.gridx = 1;

        String[] vets = {
                "Mặc định", "Bác sĩ thú y 1", "Bác sĩ thú y 2"
        };
        vetComboBox = new JComboBox<>(vets);
        inputPanel.add(vetComboBox, gbc);

        gbc.gridx = 0;
        gbc.gridy = 7;
        inputPanel.add(new JLabel("GHI CHÚ:"), gbc);
        gbc.gridx = 1;
        notesField = new JTextField(20);
        inputPanel.add(notesField, gbc);

        formPanel.add(inputPanel, BorderLayout.CENTER);

        JPanel formButtonPanel = new JPanel(new FlowLayout());
        confirmButton = new JButton();
        cancelButton = new JButton("HỦY");
        formButtonPanel.add(confirmButton);
        formButtonPanel.add(cancelButton);
        formPanel.add(formButtonPanel, BorderLayout.SOUTH);

        formDialog.setContentPane(formPanel);
    }

    public JTextField getOwnerNameField() {
        return ownerNameField;
    }

    public void showAppointments(List<Appointment> appointments) {
        tableModel.setRowCount(0);
        DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("h:mm a");

        for (Appointment appointment : appointments) {
            String petInfo = appointment.getPetName() != null ? appointment.getPetName() : "N/A";
            String ownerInfo = appointment.getOwnerName() != null ? appointment.getOwnerName() : "N/A";
            String formattedDate = appointment.getAppointmentDate().format(dateFormatter);
            String formattedTime = appointment.getAppointmentDate().format(timeFormatter);

            tableModel.addRow(new Object[]{
                    appointment.getId(),
                    petInfo,
                    ownerInfo,
                    appointment.getVetName(),
                    formattedDate,
                    formattedTime,
                    appointment.getNotes()
            });
        }
    }

    public void showAddForm() {
        clearFields();
        isAddMode = true;
        confirmButton.setText("THÊM MỚI");
        formDialog.setTitle("THÊM MỚI LỊCH HẸN");
        idField.setVisible(false);
        idLabel.setVisible(false);
        formDialog.pack();
        formDialog.setLocationRelativeTo(SwingUtilities.getWindowAncestor(this));
        formDialog.setVisible(true);
    }

    public void showUpdateForm(Appointment appointment) {
        setAppointmentFields(appointment);
        isAddMode = false;
        confirmButton.setText("CẬP NHẬT");
        formDialog.setTitle("CẬP NHẬT LỊCH HẸN");
        idField.setVisible(true);
        idLabel.setVisible(true);
        formDialog.pack();
        formDialog.setLocationRelativeTo(SwingUtilities.getWindowAncestor(this));
        formDialog.setVisible(true);
    }

    public void hideForm() {
        formDialog.setVisible(false);
    }

    public void clearFields() {
        idField.setText("");
        petIdField.setText("");
        petNameField.setText("");
        ownerNameField.setText("");
        dayField.setText("");
        monthField.setText("");
        yearField.setText("");
        notesField.setText("");
        if (timeComboBox.getItemCount() > 0) {
            timeComboBox.setSelectedIndex(0);
        }
        if (vetComboBox.getItemCount() > 0) {
            vetComboBox.setSelectedIndex(0);
        }
    }

    public void setAppointmentFields(Appointment appointment) {
        idField.setText(String.valueOf(appointment.getId()));
        petIdField.setText(String.valueOf(appointment.getPetId()));
        petNameField.setText(appointment.getPetName() != null ? appointment.getPetName() : "");
        ownerNameField.setText(appointment.getOwnerName() != null ? appointment.getOwnerName() : "");

        LocalDateTime dateTime = appointment.getAppointmentDate();
        dayField.setText(String.format("%02d", dateTime.getDayOfMonth()));
        monthField.setText(String.format("%02d", dateTime.getMonthValue()));
        yearField.setText(String.valueOf(dateTime.getYear()));

        String timeStr = dateTime.format(DateTimeFormatter.ofPattern("h:mm a"));
        timeComboBox.setSelectedItem(timeStr);

        if (appointment.getVetName() != null) {
            vetComboBox.setSelectedItem(appointment.getVetName());
        } else {
            vetComboBox.setSelectedIndex(0);
        }

        notesField.setText(appointment.getNotes() != null ? appointment.getNotes() : "");
    }

    public Appointment getAppointmentFields() {
        Appointment appointment = new Appointment();

        if (!idField.getText().isEmpty()) {
            appointment.setId(Integer.parseInt(idField.getText()));
        }

        if (!petIdField.getText().isEmpty()) {
            appointment.setPetId(Integer.parseInt(petIdField.getText()));
        }

        appointment.setPetName(petNameField.getText());
        appointment.setOwnerName(ownerNameField.getText());

        try {
            int day = Integer.parseInt(dayField.getText());
            int month = Integer.parseInt(monthField.getText());
            int year = Integer.parseInt(yearField.getText());

            String timeStr = (String) timeComboBox.getSelectedItem();
            if (timeStr != null) {
                String[] timeParts = timeStr.split(":");
                int hour = Integer.parseInt(timeParts[0].trim());
                String[] minuteAndPeriod = timeParts[1].split(" ");
                int minute = Integer.parseInt(minuteAndPeriod[0].trim());
                String period = minuteAndPeriod[1].trim();

                if (period.equals("PM") && hour != 12) {
                    hour += 12;
                } else if (period.equals("AM") && hour == 12) {
                    hour = 0;
                }

                LocalDateTime dateTime = LocalDateTime.of(year, month, day, hour, minute);
                appointment.setAppointmentDate(dateTime);
            }

            String vetName = (String) vetComboBox.getSelectedItem();
            appointment.setVetName(vetName);

        } catch (Exception e) {
            showMessage("Lỗi định dạng ngày/giờ. Vui lòng kiểm tra lại.");
            return null;
        }

        appointment.setNotes(notesField.getText());

        return appointment;
    }

    public int getSelectedAppointment() {
        int selectedRow = appointmentTable.getSelectedRow();
        if (selectedRow != -1) {
            return (int) appointmentTable.getValueAt(selectedRow, 0);
        }
        return -1;
    }

    public void showMessage(String message) {
        JOptionPane.showMessageDialog(this, message);
    }

    public void setPetInfo(String petName, String ownerName) {
        petNameField.setText(petName != null ? petName : "");
        petNameField.setEditable(false);
        petNameField.setBackground(Color.LIGHT_GRAY);

        ownerNameField.setText(ownerName != null ? ownerName : "");
        ownerNameField.setEditable(false);
        ownerNameField.setBackground(Color.LIGHT_GRAY);
    }

    public String getPetId() {
        return petIdField.getText();
    }

    public JTable getAppointmentTable() {
        return appointmentTable;
    }

    public boolean isAddMode() {
        return isAddMode;
    }

    public JButton getAddButton() {
        return addButton;
    }

    public JButton getUpdateButton() {
        return updateButton;
    }

    public JButton getDeleteButton() {
        return deleteButton;
    }

    public JButton getRefreshButton() {
        return refreshButton;
    }

    public JButton getBackButton() {
        return backButton;
    }

    public JButton getCancelButton() {
        return cancelButton;
    }

    public JButton getConfirmButton() {
        return confirmButton;
    }

    public JTextField getPetIdField() {
        return petIdField;
    }

    public void prepareForNewAppointment(int petId, String petName, String ownerName) {
        showAddForm();
        petIdField.setText(String.valueOf(petId));
        petNameField.setText(petName != null ? petName : "");
        ownerNameField.setText(ownerName != null ? ownerName : "");

        petIdField.setEditable(false);
        petIdField.setBackground(Color.LIGHT_GRAY);
        petNameField.setEditable(false);
        petNameField.setBackground(Color.LIGHT_GRAY);
        ownerNameField.setEditable(false);
        ownerNameField.setBackground(Color.LIGHT_GRAY);

        formDialog.pack();
        formDialog.setLocationRelativeTo(SwingUtilities.getWindowAncestor(this));
    }


}