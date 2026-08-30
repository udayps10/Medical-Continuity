package com.medicalcontinuity.medicalcontinuity.repositories;

import com.medicalcontinuity.medicalcontinuity.entity.PatientEncounter;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PatientEncounterRepository extends JpaRepository<PatientEncounter, Long> {

    List<PatientEncounter> findByPatientId(Long patientId);

    List<PatientEncounter> findByHospitalId(Long hospitalId);
}
