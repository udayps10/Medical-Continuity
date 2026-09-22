package com.medicalcontinuity.medicalcontinuity.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "hospital_memberships",
        uniqueConstraints = @UniqueConstraint(columnNames = {"user_id", "hospital_id"}))
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class HospitalMembership {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "hospital_id", nullable = false)
    private Hospital hospital;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private StaffRole staffRole;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private MembershipStatus status;

    public enum StaffRole {
        ADMIN, DOCTOR, NURSE, RECEPTIONIST, VOLUNTEER_COORDINATOR
    }

    public enum MembershipStatus {
        ACTIVE, SUSPENDED, REVOKED
    }
}