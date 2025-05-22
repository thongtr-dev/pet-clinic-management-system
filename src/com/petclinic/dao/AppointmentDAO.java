package com.petclinic.dao;

import com.petclinic.model.Appointment;
import java.sql.SQLException;
import java.util.List;

public interface AppointmentDAO {
    boolean add(Appointment appointment) throws SQLException;
    boolean update(Appointment appointment) throws SQLException;
    boolean delete(int id) throws SQLException;
    Appointment getById(int id) throws SQLException;
    List<Appointment> getAll() throws SQLException;
    List<Appointment> getByPetId(int petId) throws SQLException;
}