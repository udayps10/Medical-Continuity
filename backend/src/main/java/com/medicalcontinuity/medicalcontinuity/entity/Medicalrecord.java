package com.drn.entity;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "medical_records")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class MedicalRecord {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "patient_id", nullable = false)
    private Patient patient;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "hospital_id", nullable = false)
    private Hospital hospital;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "encounter_id")
    private Encounter encounter;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "created_by_user_id", nullable = false)
    private User createdByUser;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private RecordType recordType;

    @Column(nullable = false)
    private LocalDateTime recordDate;

    @Column(columnDefinition = "TEXT")
    private String summary;

    @Column(columnDefinition = "TEXT")
    private String notes;

    public enum RecordType {
        DIAGNOSIS, PRESCRIPTION, LAB_RESULT, DISCHARGE_SUMMARY, IMAGING, VACCINATION, OTHER
    }
}