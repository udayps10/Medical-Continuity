package com.medicalcontinuity.medicalcontinuity.entity;

import com.medicalcontinuity.medicalcontinuity.enums.MemoryType;
import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "memories")
public class Memory {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    @Column(name = "memory_type", nullable = false, length = 30)
    private MemoryType memoryType;

    @Column(nullable = false, length = 5000)
    private String content;

    @Column(nullable = false)
    private Integer importance;

    @Column(length = 500)
    private String source;

    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "patient_id", nullable = false)
    private Patient patient;

    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
        updatedAt = LocalDateTime.now();
    }

    @PreUpdate
    protected void onUpdate() {
        updatedAt = LocalDateTime.now();
    }

    public Memory() {}

    public Memory(MemoryType memoryType, String content, Integer importance, Patient patient) {
        this.memoryType = memoryType;
        this.content = content;
        this.importance = importance;
        this.patient = patient;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public MemoryType getMemoryType() { return memoryType; }
    public void setMemoryType(MemoryType memoryType) { this.memoryType = memoryType; }

    public String getContent() { return content; }
    public void setContent(String content) { this.content = content; }

    public Integer getImportance() { return importance; }
    public void setImportance(Integer importance) { this.importance = importance; }

    public String getSource() { return source; }
    public void setSource(String source) { this.source = source; }

    public LocalDateTime getCreatedAt() { return createdAt; }

    public LocalDateTime getUpdatedAt() { return updatedAt; }

    public Patient getPatient() { return patient; }
    public void setPatient(Patient patient) { this.patient = patient; }
}
