package com.medicalcontinuity.medicalcontinuity.service;

import com.medicalcontinuity.medicalcontinuity.entity.AccessGrant;
import com.medicalcontinuity.medicalcontinuity.entity.User;
import com.medicalcontinuity.medicalcontinuity.repository.AccessGrantRepository;
import com.medicalcontinuity.medicalcontinuity.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class AccessGrantService {

    private final AccessGrantRepository accessGrantRepository;
    private final UserRepository userRepository;

    public AccessGrant create(AccessGrant grant) {
        if (grant.getGrantedAt() == null) {
            grant.setGrantedAt(LocalDateTime.now());
        }
        if (grant.getStatus() == null) {
            grant.setStatus(AccessGrant.Status.ACTIVE);
        }
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
        LocalDateTime now = LocalDateTime.now();
        return accessGrantRepository.findByPatientIdAndStatus(patientId, AccessGrant.Status.ACTIVE)
                .stream()
                .filter(grant -> grant.getExpiresAt() == null || grant.getExpiresAt().isAfter(now))
                .collect(Collectors.toList());
    }

    public AccessGrant revoke(Long id, Long revokedByUserId) {
        AccessGrant existing = accessGrantRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Access grant not found: " + id));
        existing.setStatus(AccessGrant.Status.REVOKED);
        existing.setRevokedByUser(userRepository.findById(revokedByUserId)
                .orElseThrow(() -> new IllegalArgumentException("User not found: " + revokedByUserId)));
        existing.setRevokedAt(LocalDateTime.now());
        return accessGrantRepository.save(existing);
    }
}