package com.medicalcontinuity.medicalcontinuity.controller;

import com.medicalcontinuity.medicalcontinuity.entity.PatientEncounter;
import com.medicalcontinuity.medicalcontinuity.service.PatientEncounterService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/encounters")
public class PatientEncounterController {

    private final PatientEncounterService patientEncounterService;

    public PatientEncounterController(PatientEncounterService patientEncounterService) {
        this.patientEncounterService = patientEncounterService;
    }

    @PostMapping
    public ResponseEntity<PatientEncounter> create(@RequestBody PatientEncounter encounter) {
        PatientEncounter created = patientEncounterService.create(encounter);
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }

    @GetMapping("/{id}")
    public ResponseEntity<PatientEncounter> getById(@PathVariable Long id) {
        PatientEncounter encounter = patientEncounterService.getById(id);
        return ResponseEntity.ok(encounter);
    }

    @GetMapping
    public ResponseEntity<List<PatientEncounter>> getAll() {
        List<PatientEncounter> encounters = patientEncounterService.getAll();
        return ResponseEntity.ok(encounters);
    }

    @GetMapping("/patient/{patientId}")
    public ResponseEntity<List<PatientEncounter>> getByPatientId(@PathVariable Long patientId) {
        List<PatientEncounter> encounters = patientEncounterService.getByPatientId(patientId);
        return ResponseEntity.ok(encounters);
    }

    @GetMapping("/hospital/{hospitalId}")
    public ResponseEntity<List<PatientEncounter>> getByHospitalId(@PathVariable Long hospitalId) {
        List<PatientEncounter> encounters = patientEncounterService.getByHospitalId(hospitalId);
        return ResponseEntity.ok(encounters);
    }

    @PutMapping("/{id}")
    public ResponseEntity<PatientEncounter> update(@PathVariable Long id,
                                                    @RequestBody PatientEncounter encounter) {
        PatientEncounter updated = patientEncounterService.update(id, encounter);
        return ResponseEntity.ok(updated);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        patientEncounterService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
