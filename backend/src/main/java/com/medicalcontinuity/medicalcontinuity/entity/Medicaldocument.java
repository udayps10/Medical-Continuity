package com.drn.entity;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "medical_documents")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class MedicalDocument {

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
    @JoinColumn(name = "medical_record_id")
    private MedicalRecord medicalRecord;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "uploaded_by_user_id", nullable = false)
    private User uploadedByUser;

    @Column(nullable = false)
    private String documentType;

    @Column(nullable = false)
    private String fileName;

    @Column(nullable = false)
    private String storageKey;

    @Column(nullable = false)
    private String mimeType;

    @Column(nullable = false)
    private Long fileSize;

    @Column(nullable = false)
    private String contentHash;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private ProcessingStatus processingStatus;

    @Column(columnDefinition = "TEXT")
    private String processingError;

    @Column(nullable = false, updatable = false)
    private LocalDateTime uploadedAt;

    @Column
    private LocalDateTime processedAt;

    @PrePersist
    protected void onCreate() {
        uploadedAt = LocalDateTime.now();
    }

    public enum ProcessingStatus {
        PENDING, PROCESSING, COMPLETED, FAILED
    }
}