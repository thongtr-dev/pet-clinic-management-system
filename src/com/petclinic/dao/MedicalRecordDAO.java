package com.petclinic.dao;

import com.petclinic.model.MedicalRecord;

import java.sql.SQLException;
import java.util.List;

public interface MedicalRecordDAO {
    boolean add(MedicalRecord medicalRecord) throws SQLException;

    boolean update(MedicalRecord medicalRecord) throws SQLException;

    boolean delete(int id) throws SQLException;

    MedicalRecord getById(int id) throws SQLException;

    List<MedicalRecord> getAll() throws SQLException;

    List<MedicalRecord> getByPetId(int petId) throws SQLException;
}
