package com.petclinic.dao;

import com.petclinic.model.MedicalRecord;
import com.petclinic.util.DatabaseConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Date;
import java.util.ArrayList;
import java.util.List;

public class MedicalRecordDAOImpl implements MedicalRecordDAO {

    @Override
    public boolean add(MedicalRecord medicalRecord) throws SQLException {
        String sql = "INSERT INTO medical_records (pet_id, vaccination_date, treatment, diagnosis) VALUES (?, ?, ?, ?)";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            stmt.setInt(1, medicalRecord.getPetId());
            stmt.setDate(2, medicalRecord.getVaccinationDate() != null ?
                    Date.valueOf(medicalRecord.getVaccinationDate()) : null);
            stmt.setString(3, medicalRecord.getTreatment());
            stmt.setString(4, medicalRecord.getDiagnosis());

            int rowsAffected = stmt.executeUpdate();
            if (rowsAffected > 0) {
                try (ResultSet generatedKeys = stmt.getGeneratedKeys()) {
                    if (generatedKeys.next()) {
                        medicalRecord.setId(generatedKeys.getInt(1));
                    }
                }
                return true;
            }
        }
        return false;
    }

    @Override
    public boolean update(MedicalRecord medicalRecord) throws SQLException {
        String sql = "UPDATE medical_records SET pet_id = ?, vaccination_date = ?, treatment = ?, diagnosis = ? WHERE id = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, medicalRecord.getPetId());
            stmt.setDate(2, medicalRecord.getVaccinationDate() != null ?
                    Date.valueOf(medicalRecord.getVaccinationDate()) : null);
            stmt.setString(3, medicalRecord.getTreatment());
            stmt.setString(4, medicalRecord.getDiagnosis());
            stmt.setInt(5, medicalRecord.getId());

            int rowsAffected = stmt.executeUpdate();
            return rowsAffected > 0;
        }
    }

    @Override
    public boolean delete(int id) throws SQLException {
        String sql = "DELETE FROM medical_records WHERE id = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, id);
            int rowsAffected = stmt.executeUpdate();
            return rowsAffected > 0;
        }
    }

    @Override
    public MedicalRecord getById(int id) throws SQLException {
        String sql = "SELECT mr.*, p.name as pet_name, o.full_name as owner_name " +
                "FROM medical_records mr " +
                "LEFT JOIN pets p ON mr.pet_id = p.id " +
                "LEFT JOIN owners o ON p.owner_id = o.id " +
                "WHERE mr.id = ?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, id);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    MedicalRecord record = new MedicalRecord();
                    record.setId(rs.getInt("id"));
                    record.setPetId(rs.getInt("pet_id"));

                    Date vaccinationDate = rs.getDate("vaccination_date");
                    if (vaccinationDate != null) {
                        record.setVaccinationDate(vaccinationDate.toLocalDate());
                    }

                    record.setTreatment(rs.getString("treatment"));
                    record.setDiagnosis(rs.getString("diagnosis"));
                    record.setPetName(rs.getString("pet_name"));
                    record.setOwnerName(rs.getString("owner_name"));

                    return record;
                }
            }
        }
        return null;
    }

    @Override
    public List<MedicalRecord> getAll() throws SQLException {
        List<MedicalRecord> records = new ArrayList<>();
        String sql = "SELECT mr.*, p.name as pet_name, o.full_name as owner_name " +
                "FROM medical_records mr " +
                "LEFT JOIN pets p ON mr.pet_id = p.id " +
                "LEFT JOIN owners o ON p.owner_id = o.id " +
                "ORDER BY mr.vaccination_date DESC";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                MedicalRecord record = new MedicalRecord();
                record.setId(rs.getInt("id"));
                record.setPetId(rs.getInt("pet_id"));

                Date vaccinationDate = rs.getDate("vaccination_date");
                if (vaccinationDate != null) {
                    record.setVaccinationDate(vaccinationDate.toLocalDate());
                }

                record.setTreatment(rs.getString("treatment"));
                record.setDiagnosis(rs.getString("diagnosis"));
                record.setPetName(rs.getString("pet_name"));
                record.setOwnerName(rs.getString("owner_name"));

                records.add(record);
            }
        }
        return records;
    }

    @Override
    public List<MedicalRecord> getByPetId(int petId) throws SQLException {
        List<MedicalRecord> records = new ArrayList<>();
        String sql = "SELECT mr.*, p.name as pet_name, o.full_name as owner_name " +
                "FROM medical_records mr " +
                "LEFT JOIN pets p ON mr.pet_id = p.id " +
                "LEFT JOIN owners o ON p.owner_id = o.id " +
                "WHERE mr.pet_id = ? " +
                "ORDER BY mr.vaccination_date DESC";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, petId);
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    MedicalRecord record = new MedicalRecord();
                    record.setId(rs.getInt("id"));
                    record.setPetId(rs.getInt("pet_id"));

                    Date vaccinationDate = rs.getDate("vaccination_date");
                    if (vaccinationDate != null) {
                        record.setVaccinationDate(vaccinationDate.toLocalDate());
                    }

                    record.setTreatment(rs.getString("treatment"));
                    record.setDiagnosis(rs.getString("diagnosis"));
                    record.setPetName(rs.getString("pet_name"));
                    record.setOwnerName(rs.getString("owner_name"));

                    records.add(record);
                }
            }
        }
        return records;
    }
}
