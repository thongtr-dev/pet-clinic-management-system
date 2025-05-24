package com.petclinic.view;

import com.petclinic.model.MedicalRecord;

import java.awt.*;
import java.time.LocalDate;
import java.util.List;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;

public class MedicalRecordView extends JPanel {
    private JPanel mainPanel;
    private JTable medicalRecordTable;
    private DefaultTableModel tableModel;
    private JTextField idField;
    private JTextField petIdField;
    private JTextField petNameField;
    private JTextField ownerNameField;
    private JTextField vaccinationDateField;
    private JTextArea treatmentArea;
    private JTextArea diagnosisArea;
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

    private boolean isFilterByPet = false;
    private int filterPetId = -1;

    public MedicalRecordView() {
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
        welcomeLabel = new JLabel("QUẢN LÝ HỒ SƠ Y TẾ");
        welcomeLabel.setFont(new Font("Arial", Font.BOLD, 18));
        welcomeLabel.setHorizontalAlignment(JLabel.LEFT);
        headerPanel.add(welcomeLabel, BorderLayout.CENTER);

        backButton = new JButton("QUAY LẠI");
        headerPanel.add(backButton, BorderLayout.EAST);
        mainPanel.add(headerPanel, BorderLayout.NORTH);

        String[] columnNames = {"ID", "MÃ THÚ CƯNG", "TÊN THÚ CƯNG", "CHỦ NUÔI", "NGÀY TIÊM CHỦNG", "ĐIỀU TRỊ", "CHẨN ĐOÁN"};
        tableModel = new DefaultTableModel(columnNames, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        medicalRecordTable = new JTable(tableModel);
        medicalRecordTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        JScrollPane tableScrollPane = new JScrollPane(medicalRecordTable);
        mainPanel.add(tableScrollPane, BorderLayout.CENTER);

        JPanel buttonPanel = new JPanel(new FlowLayout());
        addButton = new JButton("THÊM MỚI");
        updateButton = new JButton("CẬP NHẬT");
        deleteButton = new JButton("XÓA BỎ");
        refreshButton = new JButton("LÀM MỚI");

        buttonPanel.add(addButton);
        buttonPanel.add(updateButton);
        buttonPanel.add(deleteButton);
        buttonPanel.add(refreshButton);
        mainPanel.add(buttonPanel, BorderLayout.SOUTH);
    }

    private void initializeFormDialog() {
        formDialog = new JDialog();
        formDialog.setTitle("Thông tin hồ sơ y tế");
        formDialog.setModal(true);
        formDialog.setSize(600, 500);
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
        inputPanel.add(new JLabel("MÃ THÚ CƯNG:"), gbc);
        gbc.gridx = 1;
        petIdField = new JTextField(10);
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
        inputPanel.add(new JLabel("NGÀY TIÊM CHỦNG (YYYY-MM-DD):"), gbc);
        gbc.gridx = 1;
        vaccinationDateField = new JTextField(20);
        inputPanel.add(vaccinationDateField, gbc);

        gbc.gridx = 0;
        gbc.gridy = 5;
        inputPanel.add(new JLabel("ĐIỀU TRỊ:"), gbc);
        gbc.gridx = 1;
        gbc.gridheight = 2;
        treatmentArea = new JTextArea(3, 20);
        treatmentArea.setLineWrap(true);
        treatmentArea.setWrapStyleWord(true);
        JScrollPane treatmentScrollPane = new JScrollPane(treatmentArea);
        inputPanel.add(treatmentScrollPane, gbc);

        gbc.gridx = 0;
        gbc.gridy = 7;
        gbc.gridheight = 1;
        inputPanel.add(new JLabel("CHẨN ĐOÁN:"), gbc);
        gbc.gridx = 1;
        gbc.gridheight = 2;
        diagnosisArea = new JTextArea(3, 20);
        diagnosisArea.setLineWrap(true);
        diagnosisArea.setWrapStyleWord(true);
        JScrollPane diagnosisScrollPane = new JScrollPane(diagnosisArea);
        inputPanel.add(diagnosisScrollPane, gbc);

        formPanel.add(inputPanel, BorderLayout.CENTER);

        JPanel formButtonPanel = new JPanel(new FlowLayout());
        confirmButton = new JButton("XÁC NHẬN");
        cancelButton = new JButton("HỦY BỎ");
        formButtonPanel.add(confirmButton);
        formButtonPanel.add(cancelButton);
        formPanel.add(formButtonPanel, BorderLayout.SOUTH);

        formDialog.setContentPane(formPanel);
    }

    public void showMedicalRecords(List<MedicalRecord> records) {
        tableModel.setRowCount(0);
        for (MedicalRecord record : records) {
            tableModel.addRow(new Object[]{
                    record.getId(),
                    record.getPetId(),
                    record.getPetName(),
                    record.getOwnerName(),
                    record.getVaccinationDate() != null ? record.getVaccinationDate().toString() : "",
                    record.getTreatment(),
                    record.getDiagnosis()
            });
        }
    }

    public void showAddForm() {
        clearFields();
        isAddMode = true;
        confirmButton.setText("THÊM MỚI");
        formDialog.setTitle("THÊM MỚI HỒ SƠ Y TẾ");
        idField.setVisible(false);
        idLabel.setVisible(false);
        formDialog.pack();
        formDialog.setLocationRelativeTo(SwingUtilities.getWindowAncestor(this));
        formDialog.setVisible(true);
    }

    public void showAddForm(int petId, String petName, String ownerName) {
        showAddForm();
        petIdField.setText(String.valueOf(petId));
        petNameField.setText(petName != null ? petName : "");
        ownerNameField.setText(ownerName != null ? ownerName : "");

        petIdField.setEditable(false);
        petIdField.setBackground(Color.LIGHT_GRAY);

        formDialog.pack();
        formDialog.setLocationRelativeTo(SwingUtilities.getWindowAncestor(this));
    }

    public void showUpdateForm(MedicalRecord record) {
        setMedicalRecordFields(record);
        isAddMode = false;
        confirmButton.setText("CẬP NHẬT");
        formDialog.setTitle("CẬP NHẬT HỒ SƠ Y TẾ");
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
        vaccinationDateField.setText("");
        treatmentArea.setText("");
        diagnosisArea.setText("");

        petIdField.setEditable(true);
        petIdField.setBackground(Color.WHITE);
    }

    public void setMedicalRecordFields(MedicalRecord record) {
        idField.setText(String.valueOf(record.getId()));
        petIdField.setText(String.valueOf(record.getPetId()));
        petNameField.setText(record.getPetName() != null ? record.getPetName() : "");
        ownerNameField.setText(record.getOwnerName() != null ? record.getOwnerName() : "");
        vaccinationDateField.setText(record.getVaccinationDate() != null ? record.getVaccinationDate().toString() : "");
        treatmentArea.setText(record.getTreatment() != null ? record.getTreatment() : "");
        diagnosisArea.setText(record.getDiagnosis() != null ? record.getDiagnosis() : "");
    }

    public MedicalRecord getMedicalRecordFields() {
        MedicalRecord record = new MedicalRecord();

        if (!idField.getText().isEmpty()) {
            record.setId(Integer.parseInt(idField.getText()));
        }

        try {
            record.setPetId(Integer.parseInt(petIdField.getText()));
        } catch (NumberFormatException e) {
            record.setPetId(0);
        }

        // Parse vaccination date
        if (!vaccinationDateField.getText().isEmpty()) {
            try {
                record.setVaccinationDate(LocalDate.parse(vaccinationDateField.getText()));
            } catch (Exception e) {
                record.setVaccinationDate(null);
            }
        }

        record.setTreatment(treatmentArea.getText());
        record.setDiagnosis(diagnosisArea.getText());

        return record;
    }

    public int getSelectedMedicalRecord() {
        int selectedRow = medicalRecordTable.getSelectedRow();
        if (selectedRow != -1) {
            return (int) medicalRecordTable.getValueAt(selectedRow, 0);
        }
        return -1;
    }

    public void showMessage(String message) {
        JOptionPane.showMessageDialog(this, message);
    }

    public void prepareForPetMedicalRecords(int petId, String petName, String ownerName) {
        isFilterByPet = true;
        filterPetId = petId;

        welcomeLabel.setText("HỒ SƠ Y TẾ CỦA " + petName.toUpperCase());

        petIdField.setText(String.valueOf(petId));
        petIdField.setEditable(false);
        petIdField.setBackground(Color.LIGHT_GRAY);

        petNameField.setText(petName);
        petNameField.setEditable(false);
        petNameField.setBackground(Color.LIGHT_GRAY);

        ownerNameField.setText(ownerName);
        ownerNameField.setEditable(false);
        ownerNameField.setBackground(Color.LIGHT_GRAY);
    }

    public void resetFilterByPet() {
        isFilterByPet = false;
        filterPetId = -1;
        welcomeLabel.setText("QUẢN LÝ HỒ SƠ Y TẾ");

        petIdField.setText("");
        petIdField.setEditable(true);
        petIdField.setBackground(UIManager.getColor("TextField.background"));

        petNameField.setText("");
        petNameField.setEditable(false);
        petNameField.setBackground(Color.LIGHT_GRAY);

        ownerNameField.setText("");
        ownerNameField.setEditable(false);
        ownerNameField.setBackground(Color.LIGHT_GRAY);
    }

    public boolean isFilterByPet() {
        return isFilterByPet;
    }

    public int getFilterPetId() {
        return filterPetId;
    }

    // Getters for components
    public JTable getMedicalRecordTable() {
        return medicalRecordTable;
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
}
