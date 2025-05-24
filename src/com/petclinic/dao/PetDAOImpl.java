package com.petclinic.dao;

import com.petclinic.model.Pet;
import com.petclinic.util.DatabaseConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Types;
import java.util.ArrayList;
import java.util.List;

public class PetDAOImpl implements PetDAO {
    @Override
    public boolean add(Pet pet) throws SQLException {
        String sql = "INSERT INTO pets (name, species, breed, age, medical_history, owner_id) VALUES (?, ?, ?, ?, ?, ?)";
        try (Connection conn = DatabaseConnection.getConnection(); PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            stmt.setString(1, pet.getName());
            stmt.setString(2, pet.getSpecies());
            stmt.setString(3, pet.getBreed());
            stmt.setInt(4, pet.getAge());
            stmt.setString(5, pet.getMedicalHistory());
            if (pet.getOwnerId() != null) {
                stmt.setInt(6, pet.getOwnerId());
            } else {
                stmt.setNull(6, Types.INTEGER);
            }
            int rowsAffected = stmt.executeUpdate();
            if (rowsAffected > 0) {
                try (ResultSet generatedKeys = stmt.getGeneratedKeys()) {
                    if (generatedKeys.next()) {
                        pet.setId(generatedKeys.getInt(1));
                    }
                }
                return true;
            }
        }
        return false;
    }

    @Override
    public boolean update(Pet pet) throws SQLException {
        String sql = "UPDATE pets SET name = ?, species = ?, breed = ?, age = ?, medical_history = ?, owner_id = ? WHERE id = ?";
        try (Connection conn = DatabaseConnection.getConnection(); PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, pet.getName());
            stmt.setString(2, pet.getSpecies());
            stmt.setString(3, pet.getBreed());
            stmt.setInt(4, pet.getAge());
            stmt.setString(5, pet.getMedicalHistory());
            if (pet.getOwnerId() != null) {
                stmt.setInt(6, pet.getOwnerId());
            } else {
                stmt.setNull(6, Types.INTEGER);
            }
            stmt.setInt(7, pet.getId());
            int rowsAffected = stmt.executeUpdate();
            return rowsAffected > 0;
        }
    }

    @Override
    public boolean delete(int id) throws SQLException {
        String sql = "DELETE FROM pets WHERE id = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, id);
            int rowsAffected = stmt.executeUpdate();
            return rowsAffected > 0;
        }
    }

    @Override
    public Pet getById(int id) throws SQLException {
        String sql = "SELECT p.*, o.full_name FROM pets p LEFT JOIN owners o ON p.owner_id = o.id WHERE p.id = ?";
        try (Connection conn = DatabaseConnection.getConnection(); PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, id);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    Pet pet = new Pet();
                    pet.setId(rs.getInt("id"));
                    pet.setName(rs.getString("name"));
                    pet.setSpecies(rs.getString("species"));
                    pet.setBreed(rs.getString("breed"));
                    pet.setAge(rs.getInt("age"));
                    pet.setMedicalHistory(rs.getString("medical_history"));
                    int ownerId = rs.getInt("owner_id");
                    if (!rs.wasNull()) {
                        pet.setOwnerId(ownerId);
                        pet.setOwnerName(rs.getString("full_name"));
                    }
                    return pet;
                }
            }
        }
        return null;
    }

    @Override
    public List<Pet> getAll() throws SQLException {
        List<Pet> pets = new ArrayList<>();
        String sql = "SELECT p.*, o.full_name FROM pets p LEFT JOIN owners o ON p.owner_id = o.id";
        try (Connection conn = DatabaseConnection.getConnection(); PreparedStatement stmt = conn.prepareStatement(sql); ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) {
                Pet pet = new Pet();
                pet.setId(rs.getInt("id"));
                pet.setName(rs.getString("name"));
                pet.setSpecies(rs.getString("species"));
                pet.setBreed(rs.getString("breed"));
                pet.setAge(rs.getInt("age"));
                pet.setMedicalHistory(rs.getString("medical_history"));
                int ownerId = rs.getInt("owner_id");
                if (!rs.wasNull()) {
                    pet.setOwnerId(ownerId);
                    pet.setOwnerName(rs.getString("full_name"));
                }
                pets.add(pet);
            }
        }
        return pets;
    }

    @Override
    public List<Pet> searchByName(String name) throws SQLException {
        List<Pet> pets = new ArrayList<>();
        String sql = "SELECT p.*, o.full_name FROM pets p LEFT JOIN owners o ON p.owner_id = o.id " +
                "WHERE LOWER(p.name) LIKE LOWER(?) ORDER BY p.name";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, "%" + name + "%");
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    Pet pet = new Pet();
                    pet.setId(rs.getInt("id"));
                    pet.setName(rs.getString("name"));
                    pet.setSpecies(rs.getString("species"));
                    pet.setBreed(rs.getString("breed"));
                    pet.setAge(rs.getInt("age"));
                    pet.setMedicalHistory(rs.getString("medical_history"));
                    int ownerId = rs.getInt("owner_id");
                    if (!rs.wasNull()) {
                        pet.setOwnerId(ownerId);
                        pet.setOwnerName(rs.getString("full_name"));
                    }
                    pets.add(pet);
                }
            }
        }
        return pets;
    }

    @Override
    public List<Pet> searchByOwnerName(String ownerName) throws SQLException {
        List<Pet> pets = new ArrayList<>();
        String sql = "SELECT p.*, o.full_name FROM pets p LEFT JOIN owners o ON p.owner_id = o.id " +
                "WHERE LOWER(o.full_name) LIKE LOWER(?) ORDER BY o.full_name, p.name";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, "%" + ownerName + "%");
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    Pet pet = new Pet();
                    pet.setId(rs.getInt("id"));
                    pet.setName(rs.getString("name"));
                    pet.setSpecies(rs.getString("species"));
                    pet.setBreed(rs.getString("breed"));
                    pet.setAge(rs.getInt("age"));
                    pet.setMedicalHistory(rs.getString("medical_history"));
                    int ownerId = rs.getInt("owner_id");
                    if (!rs.wasNull()) {
                        pet.setOwnerId(ownerId);
                        pet.setOwnerName(rs.getString("full_name"));
                    }
                    pets.add(pet);
                }
            }
        }
        return pets;
    }

    @Override
    public List<Pet> searchBySpecies(String species) throws SQLException {
        List<Pet> pets = new ArrayList<>();
        String sql = "SELECT p.*, o.full_name FROM pets p LEFT JOIN owners o ON p.owner_id = o.id " +
                "WHERE LOWER(p.species) LIKE LOWER(?) ORDER BY p.species, p.name";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, "%" + species + "%");
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    Pet pet = new Pet();
                    pet.setId(rs.getInt("id"));
                    pet.setName(rs.getString("name"));
                    pet.setSpecies(rs.getString("species"));
                    pet.setBreed(rs.getString("breed"));
                    pet.setAge(rs.getInt("age"));
                    pet.setMedicalHistory(rs.getString("medical_history"));
                    int ownerId = rs.getInt("owner_id");
                    if (!rs.wasNull()) {
                        pet.setOwnerId(ownerId);
                        pet.setOwnerName(rs.getString("full_name"));
                    }
                    pets.add(pet);
                }
            }
        }
        return pets;
    }
}