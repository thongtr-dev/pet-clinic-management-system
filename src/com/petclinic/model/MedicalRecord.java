package com.petclinic.model;

import java.time.LocalDate;

public class MedicalRecord {
    private int id;
    private int petId;
    private LocalDate vaccinationDate;
    private String treatment;
    private String diagnosis;
    private String petName;
    private String ownerName;

    public MedicalRecord() {
    }

    public MedicalRecord(int petId, LocalDate vaccinationDate, String treatment, String diagnosis) {
        this.petId = petId;
        this.vaccinationDate = vaccinationDate;
        this.treatment = treatment;
        this.diagnosis = diagnosis;
    }

    public MedicalRecord(int id, int petId, LocalDate vaccinationDate, String treatment, String diagnosis) {
        this.id = id;
        this.petId = petId;
        this.vaccinationDate = vaccinationDate;
        this.treatment = treatment;
        this.diagnosis = diagnosis;
    }

    // Getters and Setters
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getPetId() {
        return petId;
    }

    public void setPetId(int petId) {
        this.petId = petId;
    }

    public LocalDate getVaccinationDate() {
        return vaccinationDate;
    }

    public void setVaccinationDate(LocalDate vaccinationDate) {
        this.vaccinationDate = vaccinationDate;
    }

    public String getTreatment() {
        return treatment;
    }

    public void setTreatment(String treatment) {
        this.treatment = treatment;
    }

    public String getDiagnosis() {
        return diagnosis;
    }

    public void setDiagnosis(String diagnosis) {
        this.diagnosis = diagnosis;
    }

    public String getPetName() {
        return petName;
    }

    public void setPetName(String petName) {
        this.petName = petName;
    }

    public String getOwnerName() {
        return ownerName;
    }

    public void setOwnerName(String ownerName) {
        this.ownerName = ownerName;
    }

    public String getPetDisplay() {
        if (petId != 0 && petName != null) {
            return petId + " - " + petName;
        } else {
            return "";
        }
    }
}
