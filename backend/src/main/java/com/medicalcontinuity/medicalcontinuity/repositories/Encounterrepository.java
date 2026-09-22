package com.medicalcontinuity.medicalcontinuity.repository;

import com.medicalcontinuity.medicalcontinuity.entity.Encounter;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface EncounterRepository extends JpaRepository<Encounter, Long> {
    List<Encounter> findByPatientId(Long patientId);
    List<Encounter> findByHospitalId(Long hospitalId);
}