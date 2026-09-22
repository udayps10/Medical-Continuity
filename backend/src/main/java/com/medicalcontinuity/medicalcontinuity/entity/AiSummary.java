package com.medicalcontinuity.medicalcontinuity.entity;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "ai_summaries")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class AiSummary {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "patient_id", nullable = false)
    private Patient patient;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "generated_by_user_id", nullable = false)
    private User generatedByUser;

    @Column(nullable = false, columnDefinition = "TEXT")
    private String content;

    @Column(nullable = false)
    private String modelVersion;

    @Column(nullable = false, updatable = false)
    private LocalDateTime generatedAt;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Status status;

    @PrePersist
    protected void onCreate() {
        generatedAt = LocalDateTime.now();
    }

    public enum Status {
        DRAFT, PUBLISHED, SUPERSEDED
    }
}