package com.petclinic.dao;
import com.petclinic.model.Pet;

import java.sql.SQLException;
import java.util.List;

public interface PetDAO {
    void addPet(Pet pet) throws SQLException;
    void updatePet(Pet pet) throws SQLException;
    void deletePet(int petId) throws SQLException;
    Pet getPetById(int id) throws SQLException;
    List<Pet> getAllPets() throws SQLException;

    List<Pet> getPetsByOwnerId(int ownerId) throws SQLException;
}
