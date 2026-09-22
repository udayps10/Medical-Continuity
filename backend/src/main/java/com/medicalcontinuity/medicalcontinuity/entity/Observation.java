package com.medicalcontinuity.medicalcontinuity.entity;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "observations")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class Observation {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "encounter_id", nullable = false)
    private Encounter encounter;

    @Column(nullable = false)
    private String observationType;

    @Column(nullable = false)
    private String value;

    @Column
    private String unit;

    @Column(nullable = false)
    private LocalDateTime observedAt;
}