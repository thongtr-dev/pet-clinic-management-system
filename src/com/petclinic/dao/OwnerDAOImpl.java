package com.petclinic.dao;

import com.petclinic.model.Owner;
import com.petclinic.util.DatabaseConnection;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class OwnerDAOImpl implements OwnerDAO {
    private Integer getFirstAvailableId() throws SQLException {
        String sql = "SELECT MIN(t1.id + 1) AS next_id FROM owners t1 LEFT JOIN owners t2 ON t1.id + 1 = t2.id WHERE t2.id IS NULL";
        try (Connection conn = DatabaseConnection.getConnection(); 
             PreparedStatement stmt = conn.prepareStatement(sql); 
             ResultSet rs = stmt.executeQuery()) {
            if (rs.next()) {
                Integer nextId = rs.getInt("next_id");
                return rs.wasNull() ? 1 : nextId;
            }
        }
        return 1;
    }

    @Override
    public boolean add(Owner owner) throws SQLException {
        Integer availableId = getFirstAvailableId();
        String sql;
        
        if (availableId != null) {
            sql = "INSERT INTO owners (id, full_name, email, phone, address) VALUES (?, ?, ?, ?, ?)";
            try (Connection conn = DatabaseConnection.getConnection();
                 PreparedStatement stmt = conn.prepareStatement(sql)) {
                
                stmt.setInt(1, availableId);
                stmt.setString(2, owner.getName());
                stmt.setString(3, owner.getEmail());
                stmt.setString(4, owner.getPhone());
                stmt.setString(5, owner.getAddress());
                
                int rowsAffected = stmt.executeUpdate();
                if (rowsAffected > 0) {
                    owner.setId(availableId);
                    return true;
                }
            }
        } else {
            sql = "INSERT INTO owners (full_name, email, phone, address) VALUES (?, ?, ?, ?)";
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
        }
        return false;
    }

    @Override
    public boolean update(Owner owner) throws SQLException {
        String sql = "UPDATE owners SET full_name = ?, email = ?, phone = ?, address = ? WHERE id = ?";
        
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
        String sql = "SELECT o.*, COUNT(p.id) AS pet_count FROM owners o LEFT JOIN pets p ON o.id = p.owner_id WHERE o.id = ? GROUP BY o.id";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setInt(1, id);
            
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    Owner owner = new Owner();
                    owner.setId(rs.getInt("id"));
                    owner.setName(rs.getString("full_name"));
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
        String sql = "SELECT o.*, COUNT(p.id) AS pet_count FROM owners o LEFT JOIN pets p ON o.id = p.owner_id GROUP BY o.id";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            
            while (rs.next()) {
                Owner owner = new Owner();
                owner.setId(rs.getInt("id"));
                owner.setName(rs.getString("full_name"));
                owner.setEmail(rs.getString("email"));
                owner.setPhone(rs.getString("phone"));
                owner.setAddress(rs.getString("address"));
                owners.add(owner);
            }
        }
        return owners;
    }
}