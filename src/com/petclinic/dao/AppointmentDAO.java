package com.petclinic.dao;

import com.petclinic.model.Appointment;

import java.sql.SQLException;
import java.time.LocalDate;
import java.util.List;

public interface AppointmentDAO {
    boolean add(Appointment appointment) throws SQLException;

    boolean update(Appointment appointment) throws SQLException;

    boolean delete(int id) throws SQLException;

    Appointment getById(int id) throws SQLException;

    List<Appointment> getAll() throws SQLException;

    List<Appointment> getByPetId(int petId) throws SQLException;

    List<Appointment> searchByPetName(String petName) throws SQLException;

    List<Appointment> searchByOwnerName(String ownerName) throws SQLException;

    List<Appointment> searchByDate(LocalDate date) throws SQLException;

    List<Appointment> searchByDateRange(LocalDate startDate, LocalDate endDate) throws SQLException;
}