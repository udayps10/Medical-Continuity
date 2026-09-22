package com.medicalcontinuity.medicalcontinuity.repository;

import com.medicalcontinuity.medicalcontinuity.entity.AuditLog;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface AuditLogRepository extends JpaRepository<AuditLog, Long> {
    List<AuditLog> findByActorUserId(Long actorUserId);
    List<AuditLog> findByPatientId(Long patientId);
    List<AuditLog> findByHospitalId(Long hospitalId);
}