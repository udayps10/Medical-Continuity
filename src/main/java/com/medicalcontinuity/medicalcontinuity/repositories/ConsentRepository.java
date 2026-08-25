package com.medicalcontinuity.medicalcontinuity.repositories;

import com.medicalcontinuity.medicalcontinuity.entity.Consent;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ConsentRepository extends JpaRepository<Consent, Long> {
    List<Consent> findByPatientId(Long patientId);
    Optional<Consent> findByPatientIdAndHospitalId(Long patientId, Long hospitalId);
    List<Consent> findByHospitalId(Long hospitalId);
    Optional<Consent> findByPatientIdAndHospitalIdAndIsActive(Long patientId, Long hospitalId, boolean isActive);
}
