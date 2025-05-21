package com.petclinic.view;

import com.petclinic.model.Owner;
import java.awt.*;
import java.util.List;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;

public class OwnerView extends JPanel {
    private JPanel mainPanel;
    private JTable ownerTable;
    private DefaultTableModel tableModel;
    private JTextField idField;
    private JTextField fullNameField;
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
        welcomeLabel = new JLabel("QUẢN LÝ HỒ SƠ CHỦ NUÔI");
        welcomeLabel.setFont(new Font("Arial", Font.BOLD, 18));
        welcomeLabel.setHorizontalAlignment(JLabel.LEFT);
        headerPanel.add(welcomeLabel, BorderLayout.CENTER);
        backButton = new JButton("QUAY LẠI");
        headerPanel.add(backButton, BorderLayout.EAST);
        mainPanel.add(headerPanel, BorderLayout.NORTH);
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
    }

    private void initializeFormDialog() {
        formDialog = new JDialog();
        formDialog.setTitle("Thông tin chủ nuôi");
        formDialog.setModal(true);
        formDialog.setSize(500, 400);
        formDialog.setLocationRelativeTo(this);
        formPanel = new JPanel(new BorderLayout());
        JPanel inputPanel = new JPanel(new GridBagLayout());
        formPanel.add(inputPanel, BorderLayout.CENTER); 
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
        inputPanel.add(new JLabel("HỌ VÀ TÊN:"), gbc);
        gbc.gridx = 1;
        fullNameField = new JTextField(20);
        inputPanel.add(fullNameField, gbc);
        
        gbc.gridx = 0;
        gbc.gridy = 2;
        inputPanel.add(new JLabel("EMAIL:"), gbc);
        gbc.gridx = 1;
        emailField = new JTextField(20);
        inputPanel.add(emailField, gbc);

        gbc.gridx = 0;
        gbc.gridy = 3;
        inputPanel.add(new JLabel("SỐ ĐIỆN THOẠI:"), gbc);
        gbc.gridx = 1;
        phoneField = new JTextField(20);
        inputPanel.add(phoneField, gbc);

        gbc.gridx = 0;
        gbc.gridy = 4;
        inputPanel.add(new JLabel("ĐỊA CHỈ:"), gbc);
        gbc.gridx = 1;
        addressField = new JTextField(20);
        inputPanel.add(addressField, gbc);
        
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
                owner.getFullName(),
                owner.getEmail(),
                owner.getPhone(),
                owner.getAddress()
            });
        }
    }

    public void showAddForm() {
        clearFields();
        isAddMode = true;
        confirmButton.setText("THÊM MỚI");
        formDialog.setTitle("THÊM MỚI HỒ SƠ CHỦ NUÔI");
        idField.setVisible(false);
        idLabel.setVisible(false);
        formDialog.pack();
        formDialog.setLocationRelativeTo(SwingUtilities.getWindowAncestor(this));
        formDialog.setVisible(true);
    }

    public void showUpdateForm(Owner owner) {
        setOwnerFields(owner);
        isAddMode = false;
        confirmButton.setText("CẬP NHẬT");
        formDialog.setTitle("CẬP NHẬT HỒ SƠ CHỦ NUÔI");
        idField.setVisible(true);
        idLabel.setVisible(true);
        formDialog.pack();
        formDialog.setLocationRelativeTo(SwingUtilities.getWindowAncestor(this));
        formDialog.setVisible(true);
    }

    public void hideForm() { formDialog.setVisible(false); }

    public void clearFields() {
        idField.setText("");
        fullNameField.setText("");
        emailField.setText("");
        phoneField.setText("");
        addressField.setText("");
    }

    public void setOwnerFields(Owner owner) {
        idField.setText(String.valueOf(owner.getId()));
        fullNameField.setText(owner.getFullName());
        emailField.setText(owner.getEmail());
        phoneField.setText(owner.getPhone());
        addressField.setText(owner.getAddress());
    }

    public Owner getOwnerFields() {
        Owner owner = new Owner();
        if (!idField.getText().isEmpty()) {
            owner.setId(Integer.parseInt(idField.getText()));
        }
        owner.setFullName(fullNameField.getText());
        owner.setEmail(emailField.getText());
        owner.setPhone(phoneField.getText());
        owner.setAddress(addressField.getText());
        return owner;
    }

    public int getSelectedOwner() {
        int selectedRow = ownerTable.getSelectedRow();
        if (selectedRow != -1) {
            return (int) ownerTable.getValueAt(selectedRow, 0);
        }
        return -1;
    }

    public void showMessage(String message) { JOptionPane.showMessageDialog(this, message); }
    
    public JTable getOwnerTable() { return ownerTable; }
    
    public boolean isAddMode() { return isAddMode; }

    public JButton getAddButton() { return addButton; }

    public JButton getUpdateButton() { return updateButton; }

    public JButton getDeleteButton() { return deleteButton; }

    public JButton getRefreshButton() { return refreshButton; }

    public JButton getBackButton() { return backButton; }

    public JButton getCancelButton() { return cancelButton; }

    public JButton getConfirmButton() { return confirmButton; }
}