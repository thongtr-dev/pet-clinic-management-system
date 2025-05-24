package com.petclinic.model;

import java.time.LocalDateTime;

public class Appointment {
    private int id;
    private int petId;
    private String vetName;
    private LocalDateTime appointmentDate;
    private String notes;
    private String petName;
    private String ownerName;

    public Appointment() {
    }

    public Appointment(int petId, String vetName, LocalDateTime appointmentDate, String notes) {
        this.petId = petId;
        this.vetName = vetName;
        this.appointmentDate = appointmentDate;
        this.notes = notes;
    }

    public Appointment(int id, int petId, String vetName, LocalDateTime appointmentDate, String notes) {
        this.id = id;
        this.petId = petId;
        this.vetName = vetName;
        this.appointmentDate = appointmentDate;
        this.notes = notes;
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

    public String getVetName() {
        return vetName;
    }

    public void setVetName(String vetName) {
        this.vetName = vetName;
    }

    public LocalDateTime getAppointmentDate() {
        return appointmentDate;
    }

    public void setAppointmentDate(LocalDateTime appointmentDate) {
        this.appointmentDate = appointmentDate;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
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

    // Show tên Pet
    public String getPetDisplay() {
        if (petId != 0 && petName != null) {
            return petId + " - " + petName;
        } else {
            return "";
        }
    }
}