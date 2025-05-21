package com.petclinic.dao;

import com.petclinic.model.Owner;
import java.sql.SQLException;
import java.util.List;

public interface OwnerDAO {
    boolean add(Owner owner) throws SQLException;
    boolean update(Owner owner) throws SQLException;
    boolean delete(int id) throws SQLException;
    Owner getById(int id) throws SQLException;
    List<Owner> getAll() throws SQLException;
}