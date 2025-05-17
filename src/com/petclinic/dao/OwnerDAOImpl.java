package com.petclinic.dao;

import com.petclinic.model.Owner;
import com.petclinic.util.DatabaseConnection;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class OwnerDAOImpl implements OwnerDAO {
    @Override
    public boolean add(Owner owner) throws SQLException {
        String sql = "INSERT INTO owners (name, email, phone, address) VALUES (?, ?, ?, ?)";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            
            stmt.setString(1, owner.getName());
            stmt.setString(2, owner.getEmail());
            stmt.setString(3, owner.getPhone());
            stmt.setString(4, owner.getAddress());
            
            int rowsAffected = stmt.executeUpdate();
            if (rowsAffected > 0) {
                try (ResultSet generatedKeys = stmt.getGeneratedKeys()) {
                    if (generatedKeys.next()) {
                        owner.setId(generatedKeys.getInt(1));
                    }
                }
                return true;
            }
        }
        return false;
    }

    @Override
    public boolean update(Owner owner) throws SQLException {
        String sql = "UPDATE owners SET name = ?, email = ?, phone = ?, address = ? WHERE id = ?";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setString(1, owner.getName());
            stmt.setString(2, owner.getEmail());
            stmt.setString(3, owner.getPhone());
            stmt.setString(4, owner.getAddress());
            stmt.setInt(5, owner.getId());
            
            int rowsAffected = stmt.executeUpdate();
            return rowsAffected > 0;
        }
    }

    @Override
    public boolean delete(int ownerId) throws SQLException {
        // Kiểm tra xem chủ nuôi có thú cưng không trước khi xóa
        boolean hasPets = checkOwnerHasPets(ownerId);
        if (hasPets) {
            throw new SQLException("Không thể xóa chủ nuôi này vì còn thú cưng liên kết với hồ sơ.");
        }
        
        String sql = "DELETE FROM owners WHERE id = ?";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setInt(1, ownerId);
            int rowsAffected = stmt.executeUpdate();
            return rowsAffected > 0;
        }
    }
    
    private boolean checkOwnerHasPets(int ownerId) throws SQLException {
        String sql = "SELECT COUNT(*) FROM pets WHERE owner_id = ?";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setInt(1, ownerId);
            
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt(1) > 0;
                }
                return false;
            }
        }
    }

    @Override
    public Owner getById(int id) throws SQLException {
        String sql = "SELECT * FROM owners WHERE id = ?";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setInt(1, id);
            
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    Owner owner = new Owner();
                    owner.setId(rs.getInt("id"));
                    owner.setName(rs.getString("name"));
                    owner.setEmail(rs.getString("email"));
                    owner.setPhone(rs.getString("phone"));
                    owner.setAddress(rs.getString("address"));
                    return owner;
                }
            }
        }
        return null;
    }

    @Override
    public List<Owner> getAll() throws SQLException {
        List<Owner> owners = new ArrayList<>();
        String sql = "SELECT * FROM owners";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            
            while (rs.next()) {
                Owner owner = new Owner();
                owner.setId(rs.getInt("id"));
                owner.setName(rs.getString("name"));
                owner.setEmail(rs.getString("email"));
                owner.setPhone(rs.getString("phone"));
                owner.setAddress(rs.getString("address"));
                owners.add(owner);
            }
        }
        return owners;
    }
}