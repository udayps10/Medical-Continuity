package com.medicalcontinuity.medicalcontinuity.service;

import com.medicalcontinuity.medicalcontinuity.entity.HospitalMembership;
import com.medicalcontinuity.medicalcontinuity.repository.HospitalMembershipRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional
public class HospitalMembershipService {

    private final HospitalMembershipRepository hospitalMembershipRepository;

    public HospitalMembership create(HospitalMembership membership) {
        return hospitalMembershipRepository.save(membership);
    }

    @Transactional(readOnly = true)
    public Optional<HospitalMembership> findById(Long id) {
        return hospitalMembershipRepository.findById(id);
    }

    @Transactional(readOnly = true)
    public List<HospitalMembership> findByUserId(Long userId) {
        return hospitalMembershipRepository.findByUserId(userId);
    }

    @Transactional(readOnly = true)
    public List<HospitalMembership> findByHospitalId(Long hospitalId) {
        return hospitalMembershipRepository.findByHospitalId(hospitalId);
    }

    public HospitalMembership updateStatus(Long id, HospitalMembership.MembershipStatus status) {
        HospitalMembership existing = hospitalMembershipRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Membership not found: " + id));
        existing.setStatus(status);
        return hospitalMembershipRepository.save(existing);
    }

    public void delete(Long id) {
        hospitalMembershipRepository.deleteById(id);
    }
}