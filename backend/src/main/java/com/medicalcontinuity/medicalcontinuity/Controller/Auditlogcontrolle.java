package com.medicalcontinuity.medicalcontinuity.controller;

import com.medicalcontinuity.medicalcontinuity.entity.AuditLog;
import com.medicalcontinuity.medicalcontinuity.service.AuditLogService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/audit-logs")
@RequiredArgsConstructor
public class AuditLogController {

    private final AuditLogService auditLogService;

    @PostMapping
    public ResponseEntity<AuditLog> record(@RequestBody AuditLog log) {
        return ResponseEntity.ok(auditLogService.record(log));
    }

    @GetMapping("/actor/{actorUserId}")
    public ResponseEntity<List<AuditLog>> getByActor(@PathVariable Long actorUserId) {
        return ResponseEntity.ok(auditLogService.findByActorUserId(actorUserId));
    }

    @GetMapping("/patient/{patientId}")
    public ResponseEntity<List<AuditLog>> getByPatient(@PathVariable Long patientId) {
        return ResponseEntity.ok(auditLogService.findByPatientId(patientId));
    }

    @GetMapping("/hospital/{hospitalId}")
    public ResponseEntity<List<AuditLog>> getByHospital(@PathVariable Long hospitalId) {
        return ResponseEntity.ok(auditLogService.findByHospitalId(hospitalId));
    }
}package com.medicalcontinuity.medicalcontinuity.controller;

import com.medicalcontinuity.medicalcontinuity.entity.AuditLog;
import com.medicalcontinuity.medicalcontinuity.service.AuditLogService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

        import java.util.List;

@RestController
@RequestMapping("/api/audit-logs")
@RequiredArgsConstructor
public class AuditLogController {

    private final AuditLogService auditLogService;

    @PostMapping
    public ResponseEntity<AuditLog> record(@RequestBody AuditLog log) {
        return ResponseEntity.ok(auditLogService.record(log));
    }

    @GetMapping("/actor/{actorUserId}")
    public ResponseEntity<List<AuditLog>> getByActor(@PathVariable Long actorUserId) {
        return ResponseEntity.ok(auditLogService.findByActorUserId(actorUserId));
    }

    @GetMapping("/patient/{patientId}")
    public ResponseEntity<List<AuditLog>> getByPatient(@PathVariable Long patientId) {
        return ResponseEntity.ok(auditLogService.findByPatientId(patientId));
    }

    @GetMapping("/hospital/{hospitalId}")
    public ResponseEntity<List<AuditLog>> getByHospital(@PathVariable Long hospitalId) {
        return ResponseEntity.ok(auditLogService.findByHospitalId(hospitalId));
    }
}