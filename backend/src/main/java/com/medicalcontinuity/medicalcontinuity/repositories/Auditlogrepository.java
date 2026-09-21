package com.drn.repository;

import com.drn.entity.AuditLog;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface AuditLogRepository extends JpaRepository<AuditLog, Long> {
    List<AuditLog> findByActorUserId(Long actorUserId);
    List<AuditLog> findByPatientId(Long patientId);
    List<AuditLog> findByHospitalId(Long hospitalId);
}