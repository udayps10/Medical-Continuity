package com.drn.entity;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "unknown_patient_cases")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class UnknownPatientCase {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "provisional_patient_id")
    private Patient provisionalPatient;

    @Column(nullable = false, unique = true)
    private String temporaryId;

    @Column
    private Integer approximateAge;

    @Column
    private String reportedName;

    @Column
    private String gender;

    @Column
    private String phone;

    @Column
    private String village;

    @Column
    private String district;

    @Column
    private String foundLocation;

    @Column(columnDefinition = "TEXT")
    private String description;

    @Column
    private String photoRef;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "captured_by_hospital_id", nullable = false)
    private Hospital capturedByHospital;

    @Column(nullable = false, updatable = false)
    private LocalDateTime capturedAt;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "resolved_patient_id")
    private Patient resolvedPatient;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "resolved_by_user_id")
    private User resolvedByUser;

    @Column
    private LocalDateTime resolvedAt;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Status status;

    @PrePersist
    protected void onCreate() {
        capturedAt = LocalDateTime.now();
    }

    public enum Status {
        OPEN, MATCHING, RESOLVED, CLOSED_UNMATCHED
    }
}