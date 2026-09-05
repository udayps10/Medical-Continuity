package com.medicalcontinuity.medicalcontinuity.controller;

import com.medicalcontinuity.medicalcontinuity.entity.PatientMatch;
import com.medicalcontinuity.medicalcontinuity.enums.PatientMatchStatus;
import com.medicalcontinuity.medicalcontinuity.service.PatientMatchService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/patient-matches")
public class PatientMatchController {

    private final PatientMatchService patientMatchService;

    public PatientMatchController(PatientMatchService patientMatchService) {
        this.patientMatchService = patientMatchService;
    }

    @PostMapping
    public ResponseEntity<PatientMatch> createPatientMatch(@RequestBody PatientMatch patientMatch) {
        PatientMatch created = patientMatchService.createPatientMatch(patientMatch);
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }

    @GetMapping("/{id}")
    public ResponseEntity<PatientMatch> getPatientMatchById(@PathVariable Long id) {
        PatientMatch patientMatch = patientMatchService.getPatientMatchById(id);
        return ResponseEntity.ok(patientMatch);
    }

    @GetMapping("/unknown-patient/{unknownPatientId}")
    public ResponseEntity<List<PatientMatch>> getMatchesByUnknownPatientId(@PathVariable Long unknownPatientId) {
        List<PatientMatch> matches = patientMatchService.getMatchesByUnknownPatientId(unknownPatientId);
        return ResponseEntity.ok(matches);
    }

    @PutMapping("/{id}/status")
    public ResponseEntity<PatientMatch> updateStatus(@PathVariable Long id,
                                                     @RequestParam PatientMatchStatus status,
                                                     @RequestParam(required = false) String reviewedBy) {
        PatientMatch updated = patientMatchService.updateStatus(id, status, reviewedBy);
        return ResponseEntity.ok(updated);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletePatientMatch(@PathVariable Long id) {
        patientMatchService.deletePatientMatch(id);
        return ResponseEntity.noContent().build();
    }
}
