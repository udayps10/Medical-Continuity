package com.medicalcontinuity.medicalcontinuity.entity;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "patients")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class Patient {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", unique = true)
    private User user;

    @Column(unique = true)
    private String mcid; // master continuity ID

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private IdentityStatus identityStatus;

    @Column
    private String name;

    @Column
    private LocalDate dob;

    @Column
    private String gender;

    @Column
    private String phone;

    @Column
    private String village;

    @Column
    private String district;

    @Column
    private String bloodGroup;

    @Column
    private String allergies;

    @Column
    private String photoRef;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "merged_into_patient_id")
    private Patient mergedIntoPatient;

    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
    }

    public enum IdentityStatus {
        PROVISIONAL, VERIFIED, MERGED
    }
}