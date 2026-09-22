package com.medicalcontinuity.medicalcontinuity.entity;

import jakarta.persistence.*;
import lombok.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "patient_matches",
        uniqueConstraints = @UniqueConstraint(columnNames = {"match_run_id", "candidate_patient_id"}))
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class PatientMatch {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "match_run_id", nullable = false)
    private MatchRun matchRun;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "candidate_patient_id", nullable = false)
    private Patient candidatePatient;

    @Column(nullable = false, precision = 5, scale = 4)
    private BigDecimal similarityScore;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private ConfidenceLevel confidenceLevel;

    @Column(columnDefinition = "TEXT")
    private String evidence;

    @Column
    private String evidenceCoverage;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private ReviewStatus reviewStatus;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "reviewed_by_user_id")
    private User reviewedByUser;

    @Column
    private LocalDateTime reviewedAt;

    public enum ConfidenceLevel {
        LOW, MEDIUM, HIGH, VERY_HIGH
    }

    public enum ReviewStatus {
        PENDING, CONFIRMED, REJECTED
    }
}