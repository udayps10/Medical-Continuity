package com.medicalcontinuity.medicalcontinuity.controller;

import com.medicalcontinuity.medicalcontinuity.entity.MedicalRecord;
import com.medicalcontinuity.medicalcontinuity.service.MedicalRecordService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/medical-records")
public class MedicalRecordController {

    private final MedicalRecordService medicalRecordService;

    public MedicalRecordController(MedicalRecordService medicalRecordService) {
        this.medicalRecordService = medicalRecordService;
    }

    @PostMapping("/patient/{patientId}/hospital/{hospitalId}")
    public ResponseEntity<MedicalRecord> create(@PathVariable Long patientId,
                                                 @PathVariable Long hospitalId,
                                                 @RequestBody MedicalRecord record) {
        MedicalRecord created = medicalRecordService.create(patientId, hospitalId, record);
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }

    @GetMapping("/{id}")
    public ResponseEntity<MedicalRecord> getById(@PathVariable Long id) {
        MedicalRecord record = medicalRecordService.getById(id);
        return ResponseEntity.ok(record);
    }

    @GetMapping
    public ResponseEntity<List<MedicalRecord>> getAll() {
        List<MedicalRecord> records = medicalRecordService.getAll();
        return ResponseEntity.ok(records);
    }

    @GetMapping("/patient/{patientId}")
    public ResponseEntity<List<MedicalRecord>> getByPatientId(@PathVariable Long patientId) {
        List<MedicalRecord> records = medicalRecordService.getByPatientId(patientId);
        return ResponseEntity.ok(records);
    }

    @GetMapping("/hospital/{hospitalId}")
    public ResponseEntity<List<MedicalRecord>> getByHospitalId(@PathVariable Long hospitalId) {
        List<MedicalRecord> records = medicalRecordService.getByHospitalId(hospitalId);
        return ResponseEntity.ok(records);
    }

    @PutMapping("/{id}")
    public ResponseEntity<MedicalRecord> update(@PathVariable Long id,
                                                 @RequestBody MedicalRecord record) {
        MedicalRecord updated = medicalRecordService.update(id, record);
        return ResponseEntity.ok(updated);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        medicalRecordService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
