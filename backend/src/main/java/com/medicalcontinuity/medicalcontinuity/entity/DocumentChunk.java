package com.medicalcontinuity.medicalcontinuity.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "document_chunks",
        uniqueConstraints = @UniqueConstraint(columnNames = {"document_id", "chunk_index"}))
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class DocumentChunk {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "document_id", nullable = false)
    private MedicalDocument document;

    @Column(nullable = false)
    private Integer chunkIndex;

    @Column(nullable = false, columnDefinition = "TEXT")
    private String text;

    @Column
    private Integer pageNumber;

    @Column
    private String embeddingRef;

    @Column
    private String embeddingModelVersion;
}