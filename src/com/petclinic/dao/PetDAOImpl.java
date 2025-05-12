package com.petclinic.dao;

import com.petclinic.model.Pet;
import com.petclinic.util.DatabaseConnection;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class PetDAOImpl implements PetDAO {
    @Override
    public void addPet(Pet pet) throws SQLException {
        String sql = "INSERT INTO pets (name, species, breed, age, medical_history, owner_id) VALUES (?, ?, ?, ?, ?, ?)";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            ps.setString(1, pet.getName());
            ps.setString(2, pet.getSpecies());
            ps.setString(3, pet.getBreed());
            ps.setInt(4, pet.getAge());
            ps.setString(5, pet.getMedicalHistory());
            ps.setInt(6, pet.getOwnerId());

            int affectedRows = ps.executeUpdate();

            if (affectedRows == 0) {
                throw new SQLException("Creating pet failed, no rows affected.");
            }

            try (ResultSet generatedKeys = ps.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    pet.setId(generatedKeys.getInt(1));
                } else {
                    throw new SQLException("Creating pet failed, no ID obtained.");
                }
            }
        }
    }

    @Override
    public Pet getPetById(int petId) throws SQLException {
        String sql = "SELECT * FROM pets WHERE id = ?";
        Pet pet = null;

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, petId);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    pet = extractPetFromResultSet(rs);
                }
            }
        }
        return pet;
    }

    @Override
    public List<Pet> getAllPets() throws SQLException {
        String sql = "SELECT * FROM pets";
        List<Pet> pets = new ArrayList<>();

        try (Connection conn = DatabaseConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                pets.add(extractPetFromResultSet(rs));
            }
        }
        return pets;
    }

    @Override
    public List<Pet> getPetsByOwnerId(int ownerId) throws SQLException {
        String sql = "SELECT * FROM pets WHERE owner_id = ?";
        List<Pet> pets = new ArrayList<>();

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, ownerId);

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    pets.add(extractPetFromResultSet(rs));
                }
            }
        }
        return pets;
    }

    @Override
    public void updatePet(Pet pet) throws SQLException {
        String sql = "UPDATE pets SET name = ?, species = ?, breed = ?, age = ?, medical_history = ?, owner_id = ? WHERE id = ?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, pet.getName());
            ps.setString(2, pet.getSpecies());
            ps.setString(3, pet.getBreed());
            ps.setInt(4, pet.getAge());
            ps.setString(5, pet.getMedicalHistory());
            ps.setInt(6, pet.getOwnerId());
            ps.setInt(7, pet.getId());

            ps.executeUpdate();
        }
    }

    @Override
    public void deletePet(int petId) throws SQLException {
        String sql = "DELETE FROM pets WHERE id = ?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, petId);
            ps.executeUpdate();
        }
    }

    private Pet extractPetFromResultSet(ResultSet rs) throws SQLException {
        return new Pet(
                rs.getInt("id"),
                rs.getString("name"),
                rs.getString("species"),
                rs.getString("breed"),
                rs.getInt("age"),
                rs.getString("medical_history"),
                rs.getInt("owner_id")
        );
    }
}