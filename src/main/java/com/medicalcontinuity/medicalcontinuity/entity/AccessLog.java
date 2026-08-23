package com.medicalcontinuity.medicalcontinuity.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "access_logs")
public class AccessLog {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "consent_id", nullable = false)
    private Consent consent;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "accessed_by", nullable = false)
    private User accessedBy;

    @Column(nullable = false, updatable = false)
    private LocalDateTime accessedAt;

    public AccessLog() {}

    public AccessLog(Consent consent, User accessedBy) {
        this.consent = consent;
        this.accessedBy = accessedBy;
        this.accessedAt = LocalDateTime.now();
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public Consent getConsent() { return consent; }
    public void setConsent(Consent consent) { this.consent = consent; }

    public User getAccessedBy() { return accessedBy; }
    public void setAccessedBy(User accessedBy) { this.accessedBy = accessedBy; }

    public LocalDateTime getAccessedAt() { return accessedAt; }
}
