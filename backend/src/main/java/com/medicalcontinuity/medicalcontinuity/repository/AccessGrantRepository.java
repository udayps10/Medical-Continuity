package com.medicalcontinuity.medicalcontinuity.repository;

import com.medicalcontinuity.medicalcontinuity.entity.AccessGrant;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface AccessGrantRepository extends JpaRepository<AccessGrant, Long> {
    List<AccessGrant> findByPatientId(Long patientId);
    List<AccessGrant> findByHospitalId(Long hospitalId);
    List<AccessGrant> findByPatientIdAndStatus(Long patientId, AccessGrant.Status status);
}