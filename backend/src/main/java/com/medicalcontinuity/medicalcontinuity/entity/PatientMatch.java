package com.medicalcontinuity.medicalcontinuity.entity;

import com.medicalcontinuity.medicalcontinuity.enums.PatientMatchStatus;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "patient_matches")
public class PatientMatch {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "similarity_score", nullable = false, precision = 5, scale = 2)
    private BigDecimal similarityScore;

    @Column(name = "match_reason", length = 2000)
    private String matchReason;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 30)
    private PatientMatchStatus status;

    @Column(name = "reviewed_by", length = 255)
    private String reviewedBy;

    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "unknown_patient_id", nullable = false)
    @JsonIgnoreProperties({"patientMatches"})
    private UnknownPatient unknownPatient;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "candidate_patient_id", nullable = false)
    @JsonIgnoreProperties({"patientMatches", "emergencyContacts", "encounters", "medicalRecords", "medicalDocuments", "memories", "auditLogs"})
    private Patient candidatePatient;

    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
    }

    public PatientMatch() {}

    public PatientMatch(BigDecimal similarityScore, PatientMatchStatus status, UnknownPatient unknownPatient, Patient candidatePatient) {
        this.similarityScore = similarityScore;
        this.status = status;
        this.unknownPatient = unknownPatient;
        this.candidatePatient = candidatePatient;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public BigDecimal getSimilarityScore() { return similarityScore; }
    public void setSimilarityScore(BigDecimal similarityScore) { this.similarityScore = similarityScore; }

    public String getMatchReason() { return matchReason; }
    public void setMatchReason(String matchReason) { this.matchReason = matchReason; }

    public PatientMatchStatus getStatus() { return status; }
    public void setStatus(PatientMatchStatus status) { this.status = status; }

    public String getReviewedBy() { return reviewedBy; }
    public void setReviewedBy(String reviewedBy) { this.reviewedBy = reviewedBy; }

    public LocalDateTime getCreatedAt() { return createdAt; }

    public UnknownPatient getUnknownPatient() { return unknownPatient; }
    public void setUnknownPatient(UnknownPatient unknownPatient) { this.unknownPatient = unknownPatient; }

    public Patient getCandidatePatient() { return candidatePatient; }
    public void setCandidatePatient(Patient candidatePatient) { this.candidatePatient = candidatePatient; }
}
