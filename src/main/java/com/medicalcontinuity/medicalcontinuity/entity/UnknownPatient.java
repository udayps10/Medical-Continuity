package com.medicalcontinuity.medicalcontinuity.entity;

import com.medicalcontinuity.medicalcontinuity.enums.Gender;
import com.medicalcontinuity.medicalcontinuity.enums.UnknownPatientStatus;
import jakarta.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "unknown_patients")
public class UnknownPatient {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, nullable = false, length = 50)
    private String temporaryId;

    @Column(name = "approximate_age")
    private Integer approximateAge;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private Gender gender;

    @Column(length = 500)
    private String location;

    @Column(length = 2000)
    private String description;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 30)
    private UnknownPatientStatus status;

    @Column(name = "discovered_at", nullable = false, updatable = false)
    private LocalDateTime discoveredAt;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "resolved_patient_id")
    private Patient resolvedPatient;

    @OneToMany(mappedBy = "unknownPatient", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<PatientMatch> patientMatches = new ArrayList<>();

    @PrePersist
    protected void onCreate() {
        discoveredAt = LocalDateTime.now();
    }

    public UnknownPatient() {}

    public UnknownPatient(String temporaryId, Gender gender, UnknownPatientStatus status) {
        this.temporaryId = temporaryId;
        this.gender = gender;
        this.status = status;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getTemporaryId() { return temporaryId; }
    public void setTemporaryId(String temporaryId) { this.temporaryId = temporaryId; }

    public Integer getApproximateAge() { return approximateAge; }
    public void setApproximateAge(Integer approximateAge) { this.approximateAge = approximateAge; }

    public Gender getGender() { return gender; }
    public void setGender(Gender gender) { this.gender = gender; }

    public String getLocation() { return location; }
    public void setLocation(String location) { this.location = location; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public UnknownPatientStatus getStatus() { return status; }
    public void setStatus(UnknownPatientStatus status) { this.status = status; }

    public LocalDateTime getDiscoveredAt() { return discoveredAt; }

    public Patient getResolvedPatient() { return resolvedPatient; }
    public void setResolvedPatient(Patient resolvedPatient) { this.resolvedPatient = resolvedPatient; }

    public List<PatientMatch> getPatientMatches() { return patientMatches; }
    public void setPatientMatches(List<PatientMatch> patientMatches) { this.patientMatches = patientMatches; }
}
