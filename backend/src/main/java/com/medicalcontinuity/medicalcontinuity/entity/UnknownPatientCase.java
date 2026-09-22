package com.medicalcontinuity.medicalcontinuity.entity;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "unknown_patient_cases")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UnknownPatientCase {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String temporaryId;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "captured_by_hospital_id", nullable = false)
    private Hospital capturedByHospital;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "resolved_patient_id")
    private Patient resolvedPatient;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "resolved_by_user_id")
    private User resolvedByUser;

    @Column(nullable = false, updatable = false)
    private LocalDateTime capturedAt;

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
        OPEN, RESOLVED, CLOSED
    }
}
