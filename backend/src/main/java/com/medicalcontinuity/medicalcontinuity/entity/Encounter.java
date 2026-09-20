package com.drn.entity;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "encounters")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class Encounter {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "patient_id", nullable = false)
    private Patient patient;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "hospital_id", nullable = false)
    private Hospital hospital;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private EncounterType encounterType;

    @Column(nullable = false)
    private LocalDateTime arrivalTime;

    @Column
    private LocalDateTime dischargeTime;

    public enum EncounterType {
        EMERGENCY, INPATIENT, OUTPATIENT, TRIAGE, TRANSFER
    }
}
