package com.petclinic.dao;

import com.petclinic.model.Appointment;
import com.petclinic.util.DatabaseConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class AppointmentDAOImpl implements AppointmentDAO {
    @Override
    public boolean add(Appointment appointment) throws SQLException {
        String sql = "INSERT INTO appointments (pet_id, vet_name, appointment_date, notes) VALUES (?, ?, ?, ?)";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            stmt.setInt(1, appointment.getPetId());
            stmt.setString(2, appointment.getVetName());
            stmt.setTimestamp(3, Timestamp.valueOf(appointment.getAppointmentDate()));
            stmt.setString(4, appointment.getNotes());

            int rowsAffected = stmt.executeUpdate();
            if (rowsAffected > 0) {
                try (ResultSet generatedKeys = stmt.getGeneratedKeys()) {
                    if (generatedKeys.next()) {
                        appointment.setId(generatedKeys.getInt(1));
                    }
                }
                return true;
            }
        }
        return false;
    }

    @Override
    public boolean update(Appointment appointment) throws SQLException {
        String sql = "UPDATE appointments SET pet_id = ?, vet_name = ?, appointment_date = ?, notes = ? WHERE id = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, appointment.getPetId());
            stmt.setString(2, appointment.getVetName());
            stmt.setTimestamp(3, Timestamp.valueOf(appointment.getAppointmentDate()));
            stmt.setString(4, appointment.getNotes());
            stmt.setInt(5, appointment.getId());

            int rowsAffected = stmt.executeUpdate();
            return rowsAffected > 0;
        }
    }

    @Override
    public boolean delete(int id) throws SQLException {
        String sql = "DELETE FROM appointments WHERE id = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, id);
            int rowsAffected = stmt.executeUpdate();
            return rowsAffected > 0;
        }
    }

    @Override
    public Appointment getById(int id) throws SQLException {
        String sql = "SELECT a.*, p.name AS pet_name, o.full_name AS owner_name " +
                "FROM appointments a " +
                "LEFT JOIN pets p ON a.pet_id = p.id " +
                "LEFT JOIN owners o ON p.owner_id = o.id " +
                "WHERE a.id = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, id);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    Appointment appointment = new Appointment();
                    appointment.setId(rs.getInt("id"));
                    appointment.setPetId(rs.getInt("pet_id"));
                    appointment.setVetName(rs.getString("vet_name"));
                    appointment.setAppointmentDate(rs.getTimestamp("appointment_date").toLocalDateTime());
                    appointment.setNotes(rs.getString("notes"));
                    appointment.setPetName(rs.getString("pet_name"));
                    appointment.setOwnerName(rs.getString("owner_name"));
                    return appointment;
                }
            }
        }
        return null;
    }

    @Override
    public List<Appointment> getAll() throws SQLException {
        List<Appointment> appointments = new ArrayList<>();
        String sql = "SELECT a.*, p.name AS pet_name, o.full_name AS owner_name " +
                "FROM appointments a " +
                "LEFT JOIN pets p ON a.pet_id = p.id " +
                "LEFT JOIN owners o ON p.owner_id = o.id";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) {
                Appointment appointment = new Appointment();
                appointment.setId(rs.getInt("id"));
                appointment.setPetId(rs.getInt("pet_id"));
                appointment.setVetName(rs.getString("vet_name"));
                appointment.setAppointmentDate(rs.getTimestamp("appointment_date").toLocalDateTime());
                appointment.setNotes(rs.getString("notes"));
                appointment.setPetName(rs.getString("pet_name"));
                appointment.setOwnerName(rs.getString("owner_name"));
                appointments.add(appointment);
            }
        }
        return appointments;
    }

    @Override
    public List<Appointment> getByPetId(int petId) throws SQLException {
        List<Appointment> appointments = new ArrayList<>();
        String sql = "SELECT a.*, p.name AS pet_name, o.full_name AS owner_name " +
                "FROM appointments a " +
                "LEFT JOIN pets p ON a.pet_id = p.id " +
                "LEFT JOIN owners o ON p.owner_id = o.id " +
                "WHERE a.pet_id = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, petId);
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    Appointment appointment = new Appointment();
                    appointment.setId(rs.getInt("id"));
                    appointment.setPetId(rs.getInt("pet_id"));
                    appointment.setVetName(rs.getString("vet_name"));
                    appointment.setAppointmentDate(rs.getTimestamp("appointment_date").toLocalDateTime());
                    appointment.setNotes(rs.getString("notes"));
                    appointment.setPetName(rs.getString("pet_name"));
                    appointment.setOwnerName(rs.getString("owner_name"));
                    appointments.add(appointment);
                }
            }
        }
        return appointments;
    }

    @Override
    public List<Appointment> searchByPetName(String petName) throws SQLException {
        List<Appointment> appointments = new ArrayList<>();
        String sql = "SELECT a.*, p.name AS pet_name, o.full_name AS owner_name " +
                "FROM appointments a " +
                "LEFT JOIN pets p ON a.pet_id = p.id " +
                "LEFT JOIN owners o ON p.owner_id = o.id " +
                "WHERE LOWER(p.name) LIKE LOWER(?) " +
                "ORDER BY a.appointment_date DESC";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, "%" + petName + "%");
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    Appointment appointment = new Appointment();
                    appointment.setId(rs.getInt("id"));
                    appointment.setPetId(rs.getInt("pet_id"));
                    appointment.setVetName(rs.getString("vet_name"));
                    appointment.setAppointmentDate(rs.getTimestamp("appointment_date").toLocalDateTime());
                    appointment.setNotes(rs.getString("notes"));
                    appointment.setPetName(rs.getString("pet_name"));
                    appointment.setOwnerName(rs.getString("owner_name"));
                    appointments.add(appointment);
                }
            }
        }
        return appointments;
    }

    @Override
    public List<Appointment> searchByOwnerName(String ownerName) throws SQLException {
        List<Appointment> appointments = new ArrayList<>();
        String sql = "SELECT a.*, p.name AS pet_name, o.full_name AS owner_name " +
                "FROM appointments a " +
                "LEFT JOIN pets p ON a.pet_id = p.id " +
                "LEFT JOIN owners o ON p.owner_id = o.id " +
                "WHERE LOWER(o.full_name) LIKE LOWER(?) " +
                "ORDER BY a.appointment_date DESC";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, "%" + ownerName + "%");
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    Appointment appointment = new Appointment();
                    appointment.setId(rs.getInt("id"));
                    appointment.setPetId(rs.getInt("pet_id"));
                    appointment.setVetName(rs.getString("vet_name"));
                    appointment.setAppointmentDate(rs.getTimestamp("appointment_date").toLocalDateTime());
                    appointment.setNotes(rs.getString("notes"));
                    appointment.setPetName(rs.getString("pet_name"));
                    appointment.setOwnerName(rs.getString("owner_name"));
                    appointments.add(appointment);
                }
            }
        }
        return appointments;
    }

    @Override
    public List<Appointment> searchByDate(LocalDate date) throws SQLException {
        List<Appointment> appointments = new ArrayList<>();
        String sql = "SELECT a.*, p.name AS pet_name, o.full_name AS owner_name " +
                "FROM appointments a " +
                "LEFT JOIN pets p ON a.pet_id = p.id " +
                "LEFT JOIN owners o ON p.owner_id = o.id " +
                "WHERE DATE(a.appointment_date) = ? " +
                "ORDER BY a.appointment_date";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setDate(1, java.sql.Date.valueOf(date));
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    Appointment appointment = new Appointment();
                    appointment.setId(rs.getInt("id"));
                    appointment.setPetId(rs.getInt("pet_id"));
                    appointment.setVetName(rs.getString("vet_name"));
                    appointment.setAppointmentDate(rs.getTimestamp("appointment_date").toLocalDateTime());
                    appointment.setNotes(rs.getString("notes"));
                    appointment.setPetName(rs.getString("pet_name"));
                    appointment.setOwnerName(rs.getString("owner_name"));
                    appointments.add(appointment);
                }
            }
        }
        return appointments;
    }

    @Override
    public List<Appointment> searchByDateRange(LocalDate startDate, LocalDate endDate) throws SQLException {
        List<Appointment> appointments = new ArrayList<>();
        String sql = "SELECT a.*, p.name AS pet_name, o.full_name AS owner_name " +
                "FROM appointments a " +
                "LEFT JOIN pets p ON a.pet_id = p.id " +
                "LEFT JOIN owners o ON p.owner_id = o.id " +
                "WHERE DATE(a.appointment_date) BETWEEN ? AND ? " +
                "ORDER BY a.appointment_date";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setDate(1, java.sql.Date.valueOf(startDate));
            stmt.setDate(2, java.sql.Date.valueOf(endDate));
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    Appointment appointment = new Appointment();
                    appointment.setId(rs.getInt("id"));
                    appointment.setPetId(rs.getInt("pet_id"));
                    appointment.setVetName(rs.getString("vet_name"));
                    appointment.setAppointmentDate(rs.getTimestamp("appointment_date").toLocalDateTime());
                    appointment.setNotes(rs.getString("notes"));
                    appointment.setPetName(rs.getString("pet_name"));
                    appointment.setOwnerName(rs.getString("owner_name"));
                    appointments.add(appointment);
                }
            }
        }
        return appointments;
    }
}
