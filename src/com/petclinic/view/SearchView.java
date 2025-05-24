package com.petclinic.view;

import com.petclinic.model.Pet;
import com.petclinic.model.Appointment;

import java.awt.*;
import java.time.LocalDate;
import java.util.List;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;

public class SearchView extends JPanel {
    private JPanel mainPanel;
    private JTabbedPane tabbedPane;

    private JPanel petSearchPanel;
    private JTextField petNameSearchField;
    private JTextField ownerNameSearchField;
    private JTextField speciesSearchField;
    private JButton petSearchButton;
    private JButton petClearButton;
    private JTable petResultsTable;
    private DefaultTableModel petTableModel;

    private JPanel appointmentSearchPanel;
    private JTextField appointmentPetNameField;
    private JTextField appointmentOwnerNameField;
    private JTextField searchDateField;
    private JTextField startDateField;
    private JTextField endDateField;
    private JButton appointmentSearchButton;
    private JButton appointmentClearButton;
    private JTable appointmentResultsTable;
    private DefaultTableModel appointmentTableModel;

    private JButton backButton;
    private JLabel welcomeLabel;

    public SearchView() {
        initializeComponents();
        setLayout(new BorderLayout());
        add(mainPanel, BorderLayout.CENTER);
    }

    private void initializeComponents() {
        mainPanel = new JPanel(new BorderLayout());

        JPanel headerPanel = new JPanel(new BorderLayout());
        welcomeLabel = new JLabel("TÌM KIẾM VÀ LỌC DỮ LIỆU");
        welcomeLabel.setFont(new Font("Arial", Font.BOLD, 18));
        welcomeLabel.setHorizontalAlignment(JLabel.CENTER);
        headerPanel.add(welcomeLabel, BorderLayout.CENTER);

        backButton = new JButton("QUAY LẠI");
        headerPanel.add(backButton, BorderLayout.EAST);
        mainPanel.add(headerPanel, BorderLayout.NORTH);

        tabbedPane = new JTabbedPane();

        initializePetSearchPanel();
        initializeAppointmentSearchPanel();

        tabbedPane.addTab("TÌM KIẾM THÚ CƯNG", petSearchPanel);
        tabbedPane.addTab("TÌM KIẾM LỊCH HẸN", appointmentSearchPanel);

        mainPanel.add(tabbedPane, BorderLayout.CENTER);
    }

    private void initializePetSearchPanel() {
        petSearchPanel = new JPanel(new BorderLayout());

        JPanel petFormPanel = new JPanel(new GridBagLayout());
        petFormPanel.setBorder(BorderFactory.createTitledBorder("Tìm kiếm thú cưng"));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        gbc.gridx = 0;
        gbc.gridy = 0;
        petFormPanel.add(new JLabel("TÊN THÚ CƯNG:"), gbc);
        gbc.gridx = 1;
        petNameSearchField = new JTextField(20);
        petFormPanel.add(petNameSearchField, gbc);

        gbc.gridx = 0;
        gbc.gridy = 1;
        petFormPanel.add(new JLabel("TÊN CHỦ NUÔI:"), gbc);
        gbc.gridx = 1;
        ownerNameSearchField = new JTextField(20);
        petFormPanel.add(ownerNameSearchField, gbc);

        gbc.gridx = 0;
        gbc.gridy = 2;
        petFormPanel.add(new JLabel("LOÀI:"), gbc);
        gbc.gridx = 1;
        speciesSearchField = new JTextField(20);
        petFormPanel.add(speciesSearchField, gbc);

        gbc.gridx = 0;
        gbc.gridy = 3;
        gbc.gridwidth = 2;
        JPanel petButtonPanel = new JPanel(new FlowLayout());
        petSearchButton = new JButton("TÌM KIẾM");
        petClearButton = new JButton("XÓA BỘ LỌC");
        petButtonPanel.add(petSearchButton);
        petButtonPanel.add(petClearButton);
        petFormPanel.add(petButtonPanel, gbc);

        petSearchPanel.add(petFormPanel, BorderLayout.NORTH);

        String[] petColumnNames = {"ID", "TÊN", "LOÀI", "GIỐNG", "TUỔI", "TIỀN SỬ BỆNH", "CHỦ NUÔI"};
        petTableModel = new DefaultTableModel(petColumnNames, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        petResultsTable = new JTable(petTableModel);
        petResultsTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        JScrollPane petScrollPane = new JScrollPane(petResultsTable);
        petScrollPane.setBorder(BorderFactory.createTitledBorder("Kết quả tìm kiếm"));
        petSearchPanel.add(petScrollPane, BorderLayout.CENTER);
    }

    private void initializeAppointmentSearchPanel() {
        appointmentSearchPanel = new JPanel(new BorderLayout());

        JPanel appointmentFormPanel = new JPanel(new GridBagLayout());
        appointmentFormPanel.setBorder(BorderFactory.createTitledBorder("Tìm kiếm lịch hẹn"));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        gbc.gridx = 0;
        gbc.gridy = 0;
        appointmentFormPanel.add(new JLabel("TÊN THÚ CƯNG:"), gbc);
        gbc.gridx = 1;
        appointmentPetNameField = new JTextField(20);
        appointmentFormPanel.add(appointmentPetNameField, gbc);

        gbc.gridx = 0;
        gbc.gridy = 1;
        appointmentFormPanel.add(new JLabel("TÊN CHỦ NUÔI:"), gbc);
        gbc.gridx = 1;
        appointmentOwnerNameField = new JTextField(20);
        appointmentFormPanel.add(appointmentOwnerNameField, gbc);

        gbc.gridx = 0;
        gbc.gridy = 2;
        appointmentFormPanel.add(new JLabel("NGÀY HẸN (YYYY-MM-DD):"), gbc);
        gbc.gridx = 1;
        searchDateField = new JTextField(20);
        appointmentFormPanel.add(searchDateField, gbc);

        gbc.gridx = 0;
        gbc.gridy = 3;
        appointmentFormPanel.add(new JLabel("TỪ NGÀY (YYYY-MM-DD):"), gbc);
        gbc.gridx = 1;
        startDateField = new JTextField(20);
        appointmentFormPanel.add(startDateField, gbc);

        gbc.gridx = 0;
        gbc.gridy = 4;
        appointmentFormPanel.add(new JLabel("ĐẾN NGÀY (YYYY-MM-DD):"), gbc);
        gbc.gridx = 1;
        endDateField = new JTextField(20);
        appointmentFormPanel.add(endDateField, gbc);

        gbc.gridx = 0;
        gbc.gridy = 5;
        gbc.gridwidth = 2;
        JPanel appointmentButtonPanel = new JPanel(new FlowLayout());
        appointmentSearchButton = new JButton("TÌM KIẾM");
        appointmentClearButton = new JButton("XÓA BỘ LỌC");
        appointmentButtonPanel.add(appointmentSearchButton);
        appointmentButtonPanel.add(appointmentClearButton);
        appointmentFormPanel.add(appointmentButtonPanel, gbc);

        appointmentSearchPanel.add(appointmentFormPanel, BorderLayout.NORTH);

        String[] appointmentColumnNames = {"ID", "THÚ CƯNG", "CHỦ NUÔI", "BÁC SĨ", "NGÀY HẸN", "GHI CHÚ"};
        appointmentTableModel = new DefaultTableModel(appointmentColumnNames, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        appointmentResultsTable = new JTable(appointmentTableModel);
        appointmentResultsTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        JScrollPane appointmentScrollPane = new JScrollPane(appointmentResultsTable);
        appointmentScrollPane.setBorder(BorderFactory.createTitledBorder("Kết quả tìm kiếm"));
        appointmentSearchPanel.add(appointmentScrollPane, BorderLayout.CENTER);
    }

    public void showPetSearchResults(List<Pet> pets) {
        petTableModel.setRowCount(0);
        for (Pet pet : pets) {
            petTableModel.addRow(new Object[]{
                    pet.getId(),
                    pet.getName(),
                    pet.getSpecies(),
                    pet.getBreed(),
                    pet.getAge(),
                    pet.getMedicalHistory(),
                    pet.getOwnerDisplay()
            });
        }
    }

    public void showAppointmentSearchResults(List<Appointment> appointments) {
        appointmentTableModel.setRowCount(0);
        for (Appointment appointment : appointments) {
            appointmentTableModel.addRow(new Object[]{
                    appointment.getId(),
                    appointment.getPetDisplay(),
                    appointment.getOwnerName(),
                    appointment.getVetName(),
                    appointment.getAppointmentDate(),
                    appointment.getNotes()
            });
        }
    }

    public void clearPetSearchForm() {
        petNameSearchField.setText("");
        ownerNameSearchField.setText("");
        speciesSearchField.setText("");
        petTableModel.setRowCount(0);
    }

    public void clearAppointmentSearchForm() {
        appointmentPetNameField.setText("");
        appointmentOwnerNameField.setText("");
        searchDateField.setText("");
        startDateField.setText("");
        endDateField.setText("");
        appointmentTableModel.setRowCount(0);
    }

    public String getPetNameSearch() {
        return petNameSearchField.getText().trim();
    }

    public String getOwnerNameSearch() {
        return ownerNameSearchField.getText().trim();
    }

    public String getSpeciesSearch() {
        return speciesSearchField.getText().trim();
    }

    public String getAppointmentPetNameSearch() {
        return appointmentPetNameField.getText().trim();
    }

    public String getAppointmentOwnerNameSearch() {
        return appointmentOwnerNameField.getText().trim();
    }

    public String getSearchDateText() {
        return searchDateField.getText().trim();
    }

    public String getStartDateText() {
        return startDateField.getText().trim();
    }

    public String getEndDateText() {
        return endDateField.getText().trim();
    }

    public LocalDate getSearchDate() {
        try {
            String dateText = getSearchDateText();
            return dateText.isEmpty() ? null : LocalDate.parse(dateText);
        } catch (Exception e) {
            return null;
        }
    }

    public LocalDate getStartDate() {
        try {
            String dateText = getStartDateText();
            return dateText.isEmpty() ? null : LocalDate.parse(dateText);
        } catch (Exception e) {
            return null;
        }
    }

    public LocalDate getEndDate() {
        try {
            String dateText = getEndDateText();
            return dateText.isEmpty() ? null : LocalDate.parse(dateText);
        } catch (Exception e) {
            return null;
        }
    }

    public void showMessage(String message) {
        JOptionPane.showMessageDialog(this, message);
    }

    public JButton getBackButton() {
        return backButton;
    }

    public JButton getPetSearchButton() {
        return petSearchButton;
    }

    public JButton getPetClearButton() {
        return petClearButton;
    }

    public JButton getAppointmentSearchButton() {
        return appointmentSearchButton;
    }

    public JButton getAppointmentClearButton() {
        return appointmentClearButton;
    }

    public JTable getPetResultsTable() {
        return petResultsTable;
    }

    public JTable getAppointmentResultsTable() {
        return appointmentResultsTable;
    }
}
