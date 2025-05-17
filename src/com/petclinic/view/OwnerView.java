package com.petclinic.view;

import com.petclinic.model.Owner;
import java.awt.*;
import java.util.List;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;

public class OwnerView extends JFrame {
    private JPanel mainPanel;
    private JTable ownerTable;
    private DefaultTableModel tableModel;
    private JTextField idField;
    private JTextField nameField;
    private JTextField emailField;
    private JTextField phoneField;
    private JTextField addressField;
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

    public OwnerView() {
        setTitle("QUẢN LÝ HỒ SƠ CHỦ NUÔI");
        setSize(800, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        initializeComponents();
        setContentPane(mainPanel);
    }

    private void initializeComponents() {
        mainPanel = new JPanel(new BorderLayout());
        
        // Header panel
        JPanel headerPanel = new JPanel(new BorderLayout());
        welcomeLabel = new JLabel("QUẢN LÝ HỒ SƠ CHỦ NUÔI");
        welcomeLabel.setFont(new Font("Arial", Font.BOLD, 18));
        welcomeLabel.setHorizontalAlignment(JLabel.LEFT);
        headerPanel.add(welcomeLabel, BorderLayout.CENTER);
        
        backButton = new JButton("QUAY LẠI");
        headerPanel.add(backButton, BorderLayout.EAST);
        mainPanel.add(headerPanel, BorderLayout.NORTH);
        
        // Table setup
        String[] columnNames = {"ID", "TÊN", "EMAIL", "SỐ ĐIỆN THOẠI", "ĐỊA CHỈ"};
        tableModel = new DefaultTableModel(columnNames, 0) {
            @Override
            public boolean isCellEditable(int row, int column) { return false; }
        };
        
        ownerTable = new JTable(tableModel);
        ownerTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        JScrollPane tableScrollPane = new JScrollPane(ownerTable);
        mainPanel.add(tableScrollPane, BorderLayout.CENTER);
        
        // Button panel
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
        
        initializeFormDialog();
    }

    private void initializeFormDialog() {
        formDialog = new JDialog(this, "Thông tin chủ nuôi", true);
        formDialog.setSize(500, 350);
        formDialog.setLocationRelativeTo(this);
        
        formPanel = new JPanel(new BorderLayout());
        JPanel inputPanel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(5, 5, 5, 5);
        
        // ID field
        gbc.gridx = 0;
        gbc.gridy = 0;
        idLabel = new JLabel("ID:");
        inputPanel.add(idLabel, gbc);
        
        gbc.gridx = 1;
        idField = new JTextField(10);
        idField.setEditable(false);
        inputPanel.add(idField, gbc);
        
        // Name field
        gbc.gridx = 0;
        gbc.gridy = 1;
        inputPanel.add(new JLabel("TÊN:"), gbc);
        
        gbc.gridx = 1;
        nameField = new JTextField(20);
        inputPanel.add(nameField, gbc);
        
        // Email field
        gbc.gridx = 0;
        gbc.gridy = 2;
        inputPanel.add(new JLabel("EMAIL:"), gbc);
        
        gbc.gridx = 1;
        emailField = new JTextField(20);
        inputPanel.add(emailField, gbc);
        
        // Phone field
        gbc.gridx = 0;
        gbc.gridy = 3;
        inputPanel.add(new JLabel("SỐ ĐIỆN THOẠI:"), gbc);
        
        gbc.gridx = 1;
        phoneField = new JTextField(20);
        inputPanel.add(phoneField, gbc);
        
        // Address field
        gbc.gridx = 0;
        gbc.gridy = 4;
        inputPanel.add(new JLabel("ĐỊA CHỈ:"), gbc);
        
        gbc.gridx = 1;
        addressField = new JTextField(20);
        inputPanel.add(addressField, gbc);
        
        formPanel.add(inputPanel, BorderLayout.CENTER);
        
        // Form buttons
        JPanel formButtonPanel = new JPanel(new FlowLayout());
        confirmButton = new JButton();
        cancelButton = new JButton("HỦY");
        formButtonPanel.add(confirmButton);
        formButtonPanel.add(cancelButton);
        formPanel.add(formButtonPanel, BorderLayout.SOUTH);
        
        formDialog.setContentPane(formPanel);
    }

    public void showOwners(List<Owner> owners) {
        tableModel.setRowCount(0);
        for (Owner owner : owners) {
            tableModel.addRow(new Object[]{
                owner.getId(),
                owner.getName(),
                owner.getEmail(),
                owner.getPhone(),
                owner.getAddress()
            });
        }
    }

    public void showAdd() {
        clearFields();
        isAddMode = true;
        confirmButton.setText("THÊM MỚI");
        formDialog.setTitle("THÊM MỚI HỒ SƠ CHỦ NUÔI");
        idField.setVisible(false);
        idLabel.setVisible(false);
        formDialog.setVisible(true);
    }

    public void showUpdate(Owner owner) {
        setOwnerFields(owner);
        isAddMode = false;
        confirmButton.setText("CẬP NHẬT");
        formDialog.setTitle("CẬP NHẬT HỒ SƠ CHỦ NUÔI");
        idField.setVisible(true);
        idLabel.setVisible(true);
        formDialog.setVisible(true);
    }

    public void hideForm() {
        formDialog.setVisible(false);
    }

    public void clearFields() {
        idField.setText("");
        nameField.setText("");
        emailField.setText("");
        phoneField.setText("");
        addressField.setText("");
    }

    public void setOwnerFields(Owner owner) {
        idField.setText(String.valueOf(owner.getId()));
        nameField.setText(owner.getName());
        emailField.setText(owner.getEmail());
        phoneField.setText(owner.getPhone());
        addressField.setText(owner.getAddress());
    }

    public Owner getOwnerFields() {
        Owner owner = new Owner();
        if (!idField.getText().isEmpty()) {
            owner.setId(Integer.parseInt(idField.getText()));
        }
        owner.setName(nameField.getText());
        owner.setEmail(emailField.getText());
        owner.setPhone(phoneField.getText());
        owner.setAddress(addressField.getText());
        return owner;
    }

    public void showMessage(String message) {
        JOptionPane.showMessageDialog(this, message);
    }

    public boolean isAddMode() {
        return isAddMode;
    }

    public int getSelectedOwner() {
        int selectedRow = ownerTable.getSelectedRow();
        if (selectedRow != -1) {
            return (int) ownerTable.getValueAt(selectedRow, 0);
        }
        return -1;
    }

    // Getter methods for buttons
    public JButton getAddButton() { return addButton; }
    public JButton getUpdateButton() { return updateButton; }
    public JButton getDeleteButton() { return deleteButton; }
    public JButton getRefreshButton() { return refreshButton; }
    public JButton getBackButton() { return backButton; }
    public JButton getCancelButton() { return cancelButton; }
    public JButton getConfirmButton() { return confirmButton; }
    public JTable getOwnerTable() { return ownerTable; }
}