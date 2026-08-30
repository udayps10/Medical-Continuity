package com.medicalcontinuity.medicalcontinuity.entity;

import com.medicalcontinuity.medicalcontinuity.enums.RecordType;
import jakarta.persistence.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "medical_records")
public class MedicalRecord {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    @Column(name = "record_type", nullable = false, length = 30)
    private RecordType recordType;

    @Column(name = "record_date", nullable = false)
    private LocalDate recordDate;

    @Column(name = "doctor_name", length = 255)
    private String doctorName;

    @Column(length = 2000)
    private String summary;

    @Column(length = 5000)
    private String notes;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "patient_id", nullable = false)
    private Patient patient;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "hospital_id", nullable = false)
    private Hospital hospital;

    @OneToMany(mappedBy = "medicalRecord", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<MedicalDocument> documents = new ArrayList<>();

    public MedicalRecord() {}

    public MedicalRecord(RecordType recordType, LocalDate recordDate, Patient patient, Hospital hospital) {
        this.recordType = recordType;
        this.recordDate = recordDate;
        this.patient = patient;
        this.hospital = hospital;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public RecordType getRecordType() { return recordType; }
    public void setRecordType(RecordType recordType) { this.recordType = recordType; }

    public LocalDate getRecordDate() { return recordDate; }
    public void setRecordDate(LocalDate recordDate) { this.recordDate = recordDate; }

    public String getDoctorName() { return doctorName; }
    public void setDoctorName(String doctorName) { this.doctorName = doctorName; }

    public String getSummary() { return summary; }
    public void setSummary(String summary) { this.summary = summary; }

    public String getNotes() { return notes; }
    public void setNotes(String notes) { this.notes = notes; }

    public Patient getPatient() { return patient; }
    public void setPatient(Patient patient) { this.patient = patient; }

    public Hospital getHospital() { return hospital; }
    public void setHospital(Hospital hospital) { this.hospital = hospital; }

    public List<MedicalDocument> getDocuments() { return documents; }
    public void setDocuments(List<MedicalDocument> documents) { this.documents = documents; }
}
