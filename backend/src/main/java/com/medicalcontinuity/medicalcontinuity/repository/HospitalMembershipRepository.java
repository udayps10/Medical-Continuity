package com.medicalcontinuity.medicalcontinuity.repository;

import com.medicalcontinuity.medicalcontinuity.entity.HospitalMembership;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface HospitalMembershipRepository extends JpaRepository<HospitalMembership, Long> {
    List<HospitalMembership> findByUserId(Long userId);
    List<HospitalMembership> findByHospitalId(Long hospitalId);
    Optional<HospitalMembership> findByUserIdAndHospitalId(Long userId, Long hospitalId);
}