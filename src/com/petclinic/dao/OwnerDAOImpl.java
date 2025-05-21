package com.petclinic.dao;

import com.petclinic.model.Owner;
import com.petclinic.util.DatabaseConnection;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class OwnerDAOImpl implements OwnerDAO {
       @Override
    public boolean add(Owner owner) throws SQLException {
        String sql = "INSERT INTO owners (full_name, email, phone, address) VALUES (?, ?, ?, ?)";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            stmt.setString(1, owner.getFullName());
            stmt.setString(2, owner.getEmail());
            stmt.setString(3, owner.getPhone());
            stmt.setString(4, owner.getAddress());

            int affectedRows = stmt.executeUpdate();
            if (affectedRows == 0) return false;

            try (ResultSet rs = stmt.getGeneratedKeys()) {
                if (rs.next()) {
                    owner.setId(rs.getInt(1));
                }
            }
            return true;
        }
    }

    @Override
    public boolean update(Owner owner) throws SQLException {
        String sql = "UPDATE owners SET full_name=?, email=?, phone=?, address=? WHERE id=?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, owner.getFullName());
            stmt.setString(2, owner.getEmail());
            stmt.setString(3, owner.getPhone());
            stmt.setString(4, owner.getAddress());
            stmt.setInt(5, owner.getId());
            return stmt.executeUpdate() > 0;
        }
    }

    @Override
    public boolean delete(int id) throws SQLException {
        String sql = "DELETE FROM owners WHERE id = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, id);
            return stmt.executeUpdate() > 0;
        }
    }

    @Override
    public Owner getById(int id) throws SQLException {
        String sql = "SELECT * FROM owners WHERE id = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, id);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return new Owner(
                    rs.getInt("id"),
                    rs.getString("full_name"),
                    rs.getString("email"),
                    rs.getString("phone"),
                    rs.getString("address")
                );
            }
            return null;
        }
    }

    @Override
    public List<Owner> getAll() throws SQLException {
        String sql = "SELECT * FROM owners"; 
        List<Owner> owners = new ArrayList<>();
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) {
                owners.add(new Owner(
                    rs.getInt("id"),
                    rs.getString("full_name"),
                    rs.getString("email"),
                    rs.getString("phone"),
                    rs.getString("address")
                ));
            }
        }
        return owners;
    }
}