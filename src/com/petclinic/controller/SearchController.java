package com.petclinic.controller;

import com.petclinic.dao.PetDAO;
import com.petclinic.dao.AppointmentDAO;
import com.petclinic.model.Pet;
import com.petclinic.model.Appointment;
import com.petclinic.view.SearchView;

import java.sql.SQLException;
import java.time.LocalDate;
import java.util.List;
import java.util.ArrayList;

public class SearchController {
    private final SearchView searchView;
    private final PetDAO petDAO;
    private final AppointmentDAO appointmentDAO;
    private final NavigationController navigationController;

    public SearchController(SearchView searchView, PetDAO petDAO, AppointmentDAO appointmentDAO,
                            NavigationController navigationController) {
        this.searchView = searchView;
        this.petDAO = petDAO;
        this.appointmentDAO = appointmentDAO;
        this.navigationController = navigationController;
        attachEventListeners();
    }

    private void attachEventListeners() {
        searchView.getBackButton().addActionListener(e -> showDashboard());

        searchView.getPetSearchButton().addActionListener(e -> performPetSearch());
        searchView.getPetClearButton().addActionListener(e -> clearPetSearch());

        searchView.getAppointmentSearchButton().addActionListener(e -> performAppointmentSearch());
        searchView.getAppointmentClearButton().addActionListener(e -> clearAppointmentSearch());
    }

    private void showDashboard() {
        if (navigationController != null) {
            navigationController.navigateTo("USER_VIEW");
        }
    }

    private void performPetSearch() {
        try {
            List<Pet> results = new ArrayList<>();

            String petName = searchView.getPetNameSearch();
            String ownerName = searchView.getOwnerNameSearch();
            String species = searchView.getSpeciesSearch();

            if (petName.isEmpty() && ownerName.isEmpty() && species.isEmpty()) {
                searchView.showMessage("Hãy nhập ít nhất một tiêu chí tìm kiếm!");
                return;
            }

            if (!petName.isEmpty()) {
                List<Pet> petNameResults = petDAO.searchByName(petName);
                results.addAll(petNameResults);
            }

            if (!ownerName.isEmpty()) {
                List<Pet> ownerNameResults = petDAO.searchByOwnerName(ownerName);
                for (Pet pet : ownerNameResults) {
                    if (!containsPet(results, pet.getId())) {
                        results.add(pet);
                    }
                }
            }

            if (!species.isEmpty()) {
                List<Pet> speciesResults = petDAO.searchBySpecies(species);
                for (Pet pet : speciesResults) {
                    if (!containsPet(results, pet.getId())) {
                        results.add(pet);
                    }
                }
            }

            if (getActiveSearchCriteriaCount() > 1) {
                results = filterPetsByAllCriteria(results, petName, ownerName, species);
            }

            searchView.showPetSearchResults(results);

            if (results.isEmpty()) {
                searchView.showMessage("Không tìm thấy kết quả nào phù hợp!");
            } else {
                searchView.showMessage("Tìm thấy " + results.size() + " kết quả!");
            }

        } catch (SQLException ex) {
            searchView.showMessage("Database error: " + ex.getMessage());
        }
    }

    private void performAppointmentSearch() {
        try {
            List<Appointment> results = new ArrayList<>();

            String petName = searchView.getAppointmentPetNameSearch();
            String ownerName = searchView.getAppointmentOwnerNameSearch();
            LocalDate searchDate = searchView.getSearchDate();
            LocalDate startDate = searchView.getStartDate();
            LocalDate endDate = searchView.getEndDate();

            if (petName.isEmpty() && ownerName.isEmpty() && searchDate == null &&
                    startDate == null && endDate == null) {
                searchView.showMessage("Hãy nhập ít nhất một tiêu chí tìm kiếm!");
                return;
            }

            if (startDate != null && endDate != null && startDate.isAfter(endDate)) {
                searchView.showMessage("Ngày bắt đầu phải trước ngày kết thúc!");
                return;
            }

            if (!petName.isEmpty()) {
                List<Appointment> petNameResults = appointmentDAO.searchByPetName(petName);
                results.addAll(petNameResults);
            }

            if (!ownerName.isEmpty()) {
                List<Appointment> ownerNameResults = appointmentDAO.searchByOwnerName(ownerName);
                for (Appointment appointment : ownerNameResults) {
                    if (!containsAppointment(results, appointment.getId())) {
                        results.add(appointment);
                    }
                }
            }

            if (searchDate != null) {
                List<Appointment> dateResults = appointmentDAO.searchByDate(searchDate);
                for (Appointment appointment : dateResults) {
                    if (!containsAppointment(results, appointment.getId())) {
                        results.add(appointment);
                    }
                }
            }

            if (startDate != null && endDate != null) {
                List<Appointment> rangeResults = appointmentDAO.searchByDateRange(startDate, endDate);
                for (Appointment appointment : rangeResults) {
                    if (!containsAppointment(results, appointment.getId())) {
                        results.add(appointment);
                    }
                }
            }

            if (getActiveAppointmentSearchCriteriaCount() > 1) {
                results = filterAppointmentsByAllCriteria(results, petName, ownerName, searchDate, startDate, endDate);
            }

            searchView.showAppointmentSearchResults(results);

            if (results.isEmpty()) {
                searchView.showMessage("Không tìm thấy kết quả nào phù hợp!");
            } else {
                searchView.showMessage("Tìm thấy " + results.size() + " kết quả!");
            }

        } catch (SQLException ex) {
            searchView.showMessage("Database error: " + ex.getMessage());
        }
    }

    private void clearPetSearch() {
        searchView.clearPetSearchForm();
    }

    private void clearAppointmentSearch() {
        searchView.clearAppointmentSearchForm();
    }

    private boolean containsPet(List<Pet> list, int petId) {
        return list.stream().anyMatch(pet -> pet.getId() == petId);
    }

    private boolean containsAppointment(List<Appointment> list, int appointmentId) {
        return list.stream().anyMatch(appointment -> appointment.getId() == appointmentId);
    }

    private int getActiveSearchCriteriaCount() {
        int count = 0;
        if (!searchView.getPetNameSearch().isEmpty()) count++;
        if (!searchView.getOwnerNameSearch().isEmpty()) count++;
        if (!searchView.getSpeciesSearch().isEmpty()) count++;
        return count;
    }

    private int getActiveAppointmentSearchCriteriaCount() {
        int count = 0;
        if (!searchView.getAppointmentPetNameSearch().isEmpty()) count++;
        if (!searchView.getAppointmentOwnerNameSearch().isEmpty()) count++;
        if (searchView.getSearchDate() != null) count++;
        if (searchView.getStartDate() != null && searchView.getEndDate() != null) count++;
        return count;
    }

    private List<Pet> filterPetsByAllCriteria(List<Pet> pets, String petName, String ownerName, String species) {
        return pets.stream()
                .filter(pet -> {
                    boolean matches = true;

                    if (!petName.isEmpty()) {
                        matches &= pet.getName() != null &&
                                pet.getName().toLowerCase().contains(petName.toLowerCase());
                    }

                    if (!ownerName.isEmpty()) {
                        matches &= pet.getOwnerName() != null &&
                                pet.getOwnerName().toLowerCase().contains(ownerName.toLowerCase());
                    }

                    if (!species.isEmpty()) {
                        matches &= pet.getSpecies() != null &&
                                pet.getSpecies().toLowerCase().contains(species.toLowerCase());
                    }

                    return matches;
                })
                .collect(java.util.stream.Collectors.toList());
    }

    private List<Appointment> filterAppointmentsByAllCriteria(List<Appointment> appointments,
                                                              String petName, String ownerName, LocalDate searchDate, LocalDate startDate, LocalDate endDate) {
        return appointments.stream()
                .filter(appointment -> {
                    boolean matches = true;

                    if (!petName.isEmpty()) {
                        matches &= appointment.getPetName() != null &&
                                appointment.getPetName().toLowerCase().contains(petName.toLowerCase());
                    }

                    if (!ownerName.isEmpty()) {
                        matches &= appointment.getOwnerName() != null &&
                                appointment.getOwnerName().toLowerCase().contains(ownerName.toLowerCase());
                    }

                    if (searchDate != null) {
                        matches &= appointment.getAppointmentDate() != null &&
                                appointment.getAppointmentDate().toLocalDate().equals(searchDate);
                    }

                    if (startDate != null && endDate != null) {
                        LocalDate appointmentDate = appointment.getAppointmentDate().toLocalDate();
                        matches &= !appointmentDate.isBefore(startDate) && !appointmentDate.isAfter(endDate);
                    }

                    return matches;
                })
                .collect(java.util.stream.Collectors.toList());
    }
}
