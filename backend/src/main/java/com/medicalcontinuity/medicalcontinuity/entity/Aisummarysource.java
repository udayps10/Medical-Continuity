package com.drn.entity;

import jakarta.persistence.*;
import lombok.*;
import java.io.Serializable;
import java.util.Objects;

@Entity
@Table(name = "ai_summary_sources")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
@IdClass(AiSummarySource.PK.class)
public class AiSummarySource {

    @Id
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "summary_id", nullable = false)
    private AiSummary summary;

    @Id
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "chunk_id", nullable = false)
    private DocumentChunk chunk;

    @Getter @Setter @NoArgsConstructor @AllArgsConstructor
    @EqualsAndHashCode
    public static class PK implements Serializable {
        private Long summary;
        private Long chunk;
    }
}