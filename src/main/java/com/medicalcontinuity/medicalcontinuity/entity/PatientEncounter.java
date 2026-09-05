package com.medicalcontinuity.medicalcontinuity.entity;

import com.medicalcontinuity.medicalcontinuity.enums.EncounterType;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "patient_encounters")
public class PatientEncounter {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    @Column(name = "encounter_type", nullable = false, length = 30)
    private EncounterType encounterType;

    @Column(name = "arrival_time", nullable = false)
    private LocalDateTime arrivalTime;

    @Column(name = "discharge_time")
    private LocalDateTime dischargeTime;

    @Column(name = "doctor_name", length = 255)
    private String doctorName;

    @Column(length = 2000)
    private String notes;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "patient_id", nullable = false)
    @JsonIgnoreProperties({"emergencyContacts", "encounters", "medicalRecords", "medicalDocuments", "patientMatches", "memories", "auditLogs"})
    private Patient patient;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "hospital_id", nullable = false)
    @JsonIgnoreProperties({"encounters", "medicalRecords"})
    private Hospital hospital;

    public PatientEncounter() {}

    public PatientEncounter(EncounterType encounterType, LocalDateTime arrivalTime, Patient patient, Hospital hospital) {
        this.encounterType = encounterType;
        this.arrivalTime = arrivalTime;
        this.patient = patient;
        this.hospital = hospital;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public EncounterType getEncounterType() { return encounterType; }
    public void setEncounterType(EncounterType encounterType) { this.encounterType = encounterType; }

    public LocalDateTime getArrivalTime() { return arrivalTime; }
    public void setArrivalTime(LocalDateTime arrivalTime) { this.arrivalTime = arrivalTime; }

    public LocalDateTime getDischargeTime() { return dischargeTime; }
    public void setDischargeTime(LocalDateTime dischargeTime) { this.dischargeTime = dischargeTime; }

    public String getDoctorName() { return doctorName; }
    public void setDoctorName(String doctorName) { this.doctorName = doctorName; }

    public String getNotes() { return notes; }
    public void setNotes(String notes) { this.notes = notes; }

    public Patient getPatient() { return patient; }
    public void setPatient(Patient patient) { this.patient = patient; }

    public Hospital getHospital() { return hospital; }
    public void setHospital(Hospital hospital) { this.hospital = hospital; }
}
