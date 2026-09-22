package com.medicalcontinuity.medicalcontinuity.repository;

import com.medicalcontinuity.medicalcontinuity.entity.MedicalRecord;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface MedicalRecordRepository extends JpaRepository<MedicalRecord, Long> {
    List<MedicalRecord> findByPatientId(Long patientId);
    List<MedicalRecord> findByPatientIdOrderByRecordDateDesc(Long patientId);
    List<MedicalRecord> findByHospitalId(Long hospitalId);
    List<MedicalRecord> findByEncounterId(Long encounterId);
}