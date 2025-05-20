package com.petclinic.view;

import com.petclinic.model.Pet;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

public class PetView extends JPanel {
    private JPanel mainPanel;
    private JTable petTable;
    private DefaultTableModel tableModel;
    private JTextField idField;
    private JTextField nameField;
    private JTextField speciesField;
    private JTextField breedField;
    private JTextField ageField;
    private JTextArea medicalHistoryArea;
    private JTextField ownerIdField;
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

    public PetView() {
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
        welcomeLabel = new JLabel("QUẢN LÝ HỒ SƠ THÚ CƯNG");
        welcomeLabel.setFont(new Font("Arial", Font.BOLD, 18));
        welcomeLabel.setHorizontalAlignment(JLabel.LEFT);
        headerPanel.add(welcomeLabel, BorderLayout.CENTER);
        backButton = new JButton("QUAY LẠI");
        headerPanel.add(backButton, BorderLayout.EAST);
        mainPanel.add(headerPanel, BorderLayout.NORTH);
        String[] columnNames = {"ID", "TÊN", "LOÀI", "GIỐNG", "TUỔI", "TIỀN SỬ BỆNH", "CHỦ NUÔI"};
        tableModel = new DefaultTableModel(columnNames, 0) {
            @Override
            public boolean isCellEditable(int row, int column) { return false; }
        };
        petTable = new JTable(tableModel);
        petTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        JScrollPane tableScrollPane = new JScrollPane(petTable);
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
        formDialog.setTitle("Thông tin thú cưng");
        formDialog.setModal(true);
        formDialog.setSize(500, 400);
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
        inputPanel.add(new JLabel("TÊN:"), gbc);
        gbc.gridx = 1;
        nameField = new JTextField(20);
        inputPanel.add(nameField, gbc);
        gbc.gridx = 0;
        gbc.gridy = 2;
        inputPanel.add(new JLabel("LOÀI:"), gbc);
        gbc.gridx = 1;
        speciesField = new JTextField(20);
        inputPanel.add(speciesField, gbc);
        gbc.gridx = 0;
        gbc.gridy = 3;
        inputPanel.add(new JLabel("GIỐNG:"), gbc);
        gbc.gridx = 1;
        breedField = new JTextField(20);
        inputPanel.add(breedField, gbc);
        gbc.gridx = 0;
        gbc.gridy = 4;
        inputPanel.add(new JLabel("TUỔI:"), gbc);
        gbc.gridx = 1;
        ageField = new JTextField(5);
        inputPanel.add(ageField, gbc);
        gbc.gridx = 0;
        gbc.gridy = 5;
        inputPanel.add(new JLabel("TIỀN SỬ BỆNH:"), gbc);
        gbc.gridx = 1;
        medicalHistoryArea = new JTextArea(4, 20);
        medicalHistoryArea.setLineWrap(true);
        JScrollPane medicalScrollPane = new JScrollPane(medicalHistoryArea);
        inputPanel.add(medicalScrollPane, gbc);
        gbc.gridx = 0;
        gbc.gridy = 6;
        inputPanel.add(new JLabel("CHỦ NUÔI:"), gbc);
        gbc.gridx = 1;
        ownerIdField = new JTextField(10);
        inputPanel.add(ownerIdField, gbc);
        formPanel.add(inputPanel, BorderLayout.CENTER);
        JPanel formButtonPanel = new JPanel(new FlowLayout());
        confirmButton = new JButton();
        cancelButton = new JButton("HỦY");
        formButtonPanel.add(confirmButton);
        formButtonPanel.add(cancelButton);
        formPanel.add(formButtonPanel, BorderLayout.SOUTH);
        formDialog.setContentPane(formPanel);
    }

    public void showPets(List<Pet> pets) {
        tableModel.setRowCount(0);
        for (Pet pet : pets) {
            tableModel.addRow(new Object[]{pet.getId(), pet.getName(), pet.getSpecies(), pet.getBreed(), pet.getAge(), pet.getMedicalHistory(), pet.getOwnerDisplay()});
        }
    }

    public void showAddForm() {
        clearFields();
        isAddMode = true;
        confirmButton.setText("THÊM MỚI");
        formDialog.setTitle("THÊM MỚI HỒ SƠ THÚ CƯNG");
        idField.setVisible(false);
        idLabel.setVisible(false);
        formDialog.pack();
        formDialog.setLocationRelativeTo(SwingUtilities.getWindowAncestor(this));
        formDialog.setVisible(true);
    }

    public void showUpdateForm(Pet pet) {
        setPetFields(pet);
        isAddMode = false;
        confirmButton.setText("CẬP NHẬT");
        formDialog.setTitle("CẬP NHẬT HỒ SƠ THÚ CƯNG");
        idField.setVisible(true);
        idLabel.setVisible(true);
        formDialog.pack();
        formDialog.setLocationRelativeTo(SwingUtilities.getWindowAncestor(this));
        formDialog.setVisible(true);
    }

    public void hideForm() { formDialog.setVisible(false); }

    public void clearFields() {
        idField.setText("");
        nameField.setText("");
        speciesField.setText("");
        breedField.setText("");
        ageField.setText("");
        medicalHistoryArea.setText("");
        ownerIdField.setText("");
    }

    public void setPetFields(Pet pet) {
        idField.setText(String.valueOf(pet.getId()));
        nameField.setText(pet.getName());
        speciesField.setText(pet.getSpecies());
        breedField.setText(pet.getBreed());
        ageField.setText(String.valueOf(pet.getAge()));
        medicalHistoryArea.setText(pet.getMedicalHistory());
        if (pet.getOwnerId() != null) {
            ownerIdField.setText(String.valueOf(pet.getOwnerId()));
        }
        else {
            ownerIdField.setText("");
        }
    }

    public Pet getPetFields() {
        Pet pet = new Pet();
        if (!idField.getText().isEmpty()) {
            pet.setId(Integer.parseInt(idField.getText()));
        }
        pet.setName(nameField.getText());
        pet.setSpecies(speciesField.getText());
        pet.setBreed(breedField.getText());
        try {
            pet.setAge(Integer.parseInt(ageField.getText()));
        }
        catch (NumberFormatException e) {
            pet.setAge(0);
        }
        pet.setMedicalHistory(medicalHistoryArea.getText());
        if (!ownerIdField.getText().isEmpty()) {
            try {
                pet.setOwnerId(Integer.parseInt(ownerIdField.getText()));
            }
            catch (NumberFormatException e) {}
        }
        return pet;
    }

    public int getSelectedPet() {
        int selectedRow = petTable.getSelectedRow();
        if (selectedRow != -1) {
            return (int) petTable.getValueAt(selectedRow, 0);
        }
        return -1;
    }

    public void showMessage(String message) { JOptionPane.showMessageDialog(this, message); }
    
    public JTable getPetTable() { return petTable; }
    
    public boolean isAddMode() { return isAddMode; }

    public JButton getAddButton() { return addButton; }

    public JButton getUpdateButton() { return updateButton; }

    public JButton getDeleteButton() { return deleteButton; }

    public JButton getRefreshButton() { return refreshButton; }

    public JButton getBackButton() { return backButton; }

    public JButton getCancelButton() { return cancelButton; }

    public JButton getConfirmButton() { return confirmButton; }
}