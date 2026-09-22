package com.medicalcontinuity.medicalcontinuity.entity;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "access_grants")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class AccessGrant {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "patient_id", nullable = false)
    private Patient patient;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "hospital_id", nullable = false)
    private Hospital hospital;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "initiated_by_user_id", nullable = false)
    private User initiatedByUser;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "granted_by_user_id")
    private User grantedByUser;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private GrantedVia grantedVia;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Scope scope;

    @Column
    private String consentEvidenceRef;

    @Column(nullable = false)
    private String reasonCode;

    @Column(nullable = false)
    private LocalDateTime grantedAt;

    @Column
    private LocalDateTime expiresAt;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "revoked_by_user_id")
    private User revokedByUser;

    @Column
    private LocalDateTime revokedAt;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Status status;

    public enum GrantedVia {
        PATIENT_CONSENT, EMERGENCY_OVERRIDE, GUARDIAN_CONSENT, ADMIN_GRANT
    }

    public enum Scope {
        FULL_RECORD, SUMMARY_ONLY, ENCOUNTER_SPECIFIC
    }

    public enum Status {
        ACTIVE, EXPIRED, REVOKED
    }
}