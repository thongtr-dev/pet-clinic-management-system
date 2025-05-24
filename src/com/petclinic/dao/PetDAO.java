package com.petclinic.dao;

import com.petclinic.model.Pet;

import java.sql.SQLException;
import java.util.List;

public interface PetDAO {
    boolean add(Pet pet) throws SQLException;

    boolean update(Pet pet) throws SQLException;

    boolean delete(int id) throws SQLException;

    Pet getById(int id) throws SQLException;

    List<Pet> getAll() throws SQLException;

    List<Pet> searchByName(String name) throws SQLException;

    List<Pet> searchByOwnerName(String ownerName) throws SQLException;

    List<Pet> searchBySpecies(String species) throws SQLException;
}