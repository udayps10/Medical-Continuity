package com.medicalcontinuity.medicalcontinuity.service;

import com.medicalcontinuity.medicalcontinuity.entity.AuditLog;
import com.medicalcontinuity.medicalcontinuity.repository.AuditLogRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class AuditLogService {

    private final AuditLogRepository auditLogRepository;

    public AuditLog record(AuditLog log) {
        return auditLogRepository.save(log);
    }

    @Transactional(readOnly = true)
    public List<AuditLog> findByActorUserId(Long actorUserId) {
        return auditLogRepository.findByActorUserId(actorUserId);
    }

    @Transactional(readOnly = true)
    public List<AuditLog> findByPatientId(Long patientId) {
        return auditLogRepository.findByPatientId(patientId);
    }

    @Transactional(readOnly = true)
    public List<AuditLog> findByHospitalId(Long hospitalId) {
        return auditLogRepository.findByHospitalId(hospitalId);
    }
}