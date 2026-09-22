package com.medicalcontinuity.medicalcontinuity.controller;

import com.medicalcontinuity.medicalcontinuity.entity.Encounter;
import com.medicalcontinuity.medicalcontinuity.service.EncounterService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/encounters")
@RequiredArgsConstructor
public class EncounterController {

    private final EncounterService encounterService;

    @PostMapping
    public ResponseEntity<Encounter> create(@RequestBody Encounter encounter) {
        return ResponseEntity.ok(encounterService.create(encounter));
    }

    @GetMapping("/{id}")
    public ResponseEntity<Encounter> getById(@PathVariable Long id) {
        return encounterService.findById(id)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @GetMapping("/patient/{patientId}")
    public ResponseEntity<List<Encounter>> getByPatient(@PathVariable Long patientId) {
        return ResponseEntity.ok(encounterService.findByPatientId(patientId));
    }

    @GetMapping("/hospital/{hospitalId}")
    public ResponseEntity<List<Encounter>> getByHospital(@PathVariable Long hospitalId) {
        return ResponseEntity.ok(encounterService.findByHospitalId(hospitalId));
    }

    @PatchMapping("/{id}/discharge")
    public ResponseEntity<Encounter> discharge(@PathVariable Long id) {
        return ResponseEntity.ok(encounterService.discharge(id));
    }
}