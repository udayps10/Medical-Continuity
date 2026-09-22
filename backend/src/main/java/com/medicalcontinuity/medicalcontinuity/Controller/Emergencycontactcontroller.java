package com.medicalcontinuity.medicalcontinuity.controller;

import com.medicalcontinuity.medicalcontinuity.entity.EmergencyContact;
import com.medicalcontinuity.medicalcontinuity.service.EmergencyContactService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/emergency-contacts")
@RequiredArgsConstructor
public class EmergencyContactController {

    private final EmergencyContactService emergencyContactService;

    @PostMapping
    public ResponseEntity<EmergencyContact> create(@RequestBody EmergencyContact contact) {
        return ResponseEntity.ok(emergencyContactService.create(contact));
    }

    @GetMapping("/patient/{patientId}")
    public ResponseEntity<List<EmergencyContact>> getByPatient(@PathVariable Long patientId) {
        return ResponseEntity.ok(emergencyContactService.findByPatientId(patientId));
    }

    @PutMapping("/{id}")
    public ResponseEntity<EmergencyContact> update(@PathVariable Long id, @RequestBody EmergencyContact contact) {
        return ResponseEntity.ok(emergencyContactService.update(id, contact));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        emergencyContactService.delete(id);
        return ResponseEntity.noContent().build();
    }
}