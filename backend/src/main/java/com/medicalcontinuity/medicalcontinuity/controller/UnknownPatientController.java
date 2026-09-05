package com.medicalcontinuity.medicalcontinuity.controller;

import com.medicalcontinuity.medicalcontinuity.entity.UnknownPatient;
import com.medicalcontinuity.medicalcontinuity.service.UnknownPatientService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/unknown-patients")
public class UnknownPatientController {

    private final UnknownPatientService unknownPatientService;

    public UnknownPatientController(UnknownPatientService unknownPatientService) {
        this.unknownPatientService = unknownPatientService;
    }

    @PostMapping
    public ResponseEntity<UnknownPatient> createUnknownPatient(@RequestBody UnknownPatient unknownPatient) {
        UnknownPatient created = unknownPatientService.createUnknownPatient(unknownPatient);
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }

    @GetMapping
    public ResponseEntity<List<UnknownPatient>> getAllUnknownPatients() {
        List<UnknownPatient> unknownPatients = unknownPatientService.getAllUnknownPatients();
        return ResponseEntity.ok(unknownPatients);
    }

    @GetMapping("/{id}")
    public ResponseEntity<UnknownPatient> getUnknownPatientById(@PathVariable Long id) {
        UnknownPatient unknownPatient = unknownPatientService.getUnknownPatientById(id);
        return ResponseEntity.ok(unknownPatient);
    }

    @GetMapping("/temporary/{temporaryId}")
    public ResponseEntity<UnknownPatient> getUnknownPatientByTemporaryId(@PathVariable String temporaryId) {
        UnknownPatient unknownPatient = unknownPatientService.getUnknownPatientByTemporaryId(temporaryId);
        return ResponseEntity.ok(unknownPatient);
    }

    @PutMapping("/{id}")
    public ResponseEntity<UnknownPatient> updateUnknownPatient(@PathVariable Long id, @RequestBody UnknownPatient unknownPatient) {
        UnknownPatient updated = unknownPatientService.updateUnknownPatient(id, unknownPatient);
        return ResponseEntity.ok(updated);
    }

    @PutMapping("/{id}/resolve/{patientId}")
    public ResponseEntity<UnknownPatient> resolveUnknownPatient(@PathVariable Long id, @PathVariable Long patientId) {
        UnknownPatient resolved = unknownPatientService.resolveUnknownPatient(id, patientId);
        return ResponseEntity.ok(resolved);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteUnknownPatient(@PathVariable Long id) {
        unknownPatientService.deleteUnknownPatient(id);
        return ResponseEntity.noContent().build();
    }
}
