package com.petclinic.model;

public class Pet {
    private int id;
    private String name;
    private String species;
    private String breed;
    private int age;
    private String medicalHistory;
    private Integer ownerId;
    private String ownerName;

    public Pet() {}

    public Pet(String name, String species, String breed, int age, String medicalHistory, Integer ownerId) {
        this.name = name;
        this.species = species;
        this.breed = breed;
        this.age = age;
        this.medicalHistory = medicalHistory;
        this.ownerId = ownerId;
    }

    public Pet(int id, String name, String species, String breed, int age, String medicalHistory, Integer ownerId) {
        this.id = id;
        this.name = name;
        this.species = species;
        this.breed = breed;
        this.age = age;
        this.medicalHistory = medicalHistory;
        this.ownerId = ownerId;
    }

    public int getId() { return id; }

    public String getName() { return name; }

    public String getSpecies() { return species; }

    public String getBreed() { return breed; }

    public int getAge() { return age; }

    public String getMedicalHistory() { return medicalHistory; }

    public Integer getOwnerId() { return ownerId; }

    public String getOwnerName() { return ownerName; }

    public void setId(int id) { this.id = id; }

    public void setName(String name) { this.name = name; }

    public void setSpecies(String species) { this.species = species; }

    public void setBreed(String breed) { this.breed = breed; }

    public void setAge(int age) { this.age = age; }

    public void setMedicalHistory(String medicalHistory) { this.medicalHistory = medicalHistory; }

    public void setOwnerId(Integer ownerId) { this.ownerId = ownerId; }

    public void setOwnerName(String ownerName) { this.ownerName = ownerName; }
    
    public String getOwnerDisplay() {
        if (ownerId != null && ownerName != null) {
            return ownerId + " - " + ownerName;
        }
        else {
            return "";
        }
    }
}