package com.medicalcontinuity.medicalcontinuity.controller;

import com.medicalcontinuity.medicalcontinuity.entity.MedicalRecord;
import com.medicalcontinuity.medicalcontinuity.service.MedicalRecordService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/medical-records")
@RequiredArgsConstructor
public class MedicalRecordController {

    private final MedicalRecordService medicalRecordService;

    @PostMapping
    public ResponseEntity<MedicalRecord> create(@RequestBody MedicalRecord record) {
        return ResponseEntity.ok(medicalRecordService.create(record));
    }

    @GetMapping("/{id}")
    public ResponseEntity<MedicalRecord> getById(@PathVariable Long id) {
        return medicalRecordService.findById(id)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    // Date-wise view for a doctor. Requires findByPatientIdOrderByRecordDateDesc
    // on MedicalRecordRepository/Service (see AI-part discussion).
    @GetMapping("/patient/{patientId}")
    public ResponseEntity<List<MedicalRecord>> getByPatient(@PathVariable Long patientId) {
        return ResponseEntity.ok(medicalRecordService.findByPatientId(patientId));
    }

    @GetMapping("/encounter/{encounterId}")
    public ResponseEntity<List<MedicalRecord>> getByEncounter(@PathVariable Long encounterId) {
        return ResponseEntity.ok(medicalRecordService.findByEncounterId(encounterId));
    }

    @PutMapping("/{id}")
    public ResponseEntity<MedicalRecord> update(@PathVariable Long id, @RequestBody MedicalRecord record) {
        return ResponseEntity.ok(medicalRecordService.update(id, record));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        medicalRecordService.delete(id);
        return ResponseEntity.noContent().build();
    }
}