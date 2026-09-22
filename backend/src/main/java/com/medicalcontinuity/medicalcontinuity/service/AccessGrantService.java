package com.medicalcontinuity.medicalcontinuity.service;

import com.medicalcontinuity.medicalcontinuity.entity.AccessGrant;
import com.medicalcontinuity.medicalcontinuity.repository.AccessGrantRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional
public class AccessGrantService {

    private final AccessGrantRepository accessGrantRepository;

    public AccessGrant create(AccessGrant grant) {
        grant.setGrantedAt(LocalDateTime.now());
        grant.setStatus(AccessGrant.Status.ACTIVE);
        return accessGrantRepository.save(grant);
    }

    @Transactional(readOnly = true)
    public Optional<AccessGrant> findById(Long id) {
        return accessGrantRepository.findById(id);
    }

    @Transactional(readOnly = true)
    public List<AccessGrant> findByPatientId(Long patientId) {
        return accessGrantRepository.findByPatientId(patientId);
    }

    @Transactional(readOnly = true)
    public List<AccessGrant> findActiveByPatientId(Long patientId) {
        return accessGrantRepository.findByPatientIdAndStatus(patientId, AccessGrant.Status.ACTIVE);
    }

    public AccessGrant revoke(Long id, Long revokedByUserId) {
        AccessGrant existing = accessGrantRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Access grant not found: " + id));
        existing.setStatus(AccessGrant.Status.REVOKED);
        existing.setRevokedAt(LocalDateTime.now());
        return accessGrantRepository.save(existing);
    }
}