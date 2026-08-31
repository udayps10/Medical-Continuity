package com.medicalcontinuity.medicalcontinuity.controller;

import com.medicalcontinuity.medicalcontinuity.entity.EmergencyContact;
import com.medicalcontinuity.medicalcontinuity.service.EmergencyContactService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/emergency-contacts")
public class EmergencyContactController {

    private final EmergencyContactService emergencyContactService;

    public EmergencyContactController(EmergencyContactService emergencyContactService) {
        this.emergencyContactService = emergencyContactService;
    }

    @PostMapping
    public ResponseEntity<EmergencyContact> create(@RequestBody EmergencyContact contact) {
        EmergencyContact created = emergencyContactService.create(contact);
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }

    @GetMapping("/{id}")
    public ResponseEntity<EmergencyContact> getById(@PathVariable Long id) {
        EmergencyContact contact = emergencyContactService.getById(id);
        return ResponseEntity.ok(contact);
    }

    @GetMapping
    public ResponseEntity<List<EmergencyContact>> getAll() {
        List<EmergencyContact> contacts = emergencyContactService.getAll();
        return ResponseEntity.ok(contacts);
    }

    @GetMapping("/patient/{patientId}")
    public ResponseEntity<List<EmergencyContact>> getByPatientId(@PathVariable Long patientId) {
        List<EmergencyContact> contacts = emergencyContactService.getByPatientId(patientId);
        return ResponseEntity.ok(contacts);
    }

    @PutMapping("/{id}")
    public ResponseEntity<EmergencyContact> update(@PathVariable Long id,
                                                    @RequestBody EmergencyContact contact) {
        EmergencyContact updated = emergencyContactService.update(id, contact);
        return ResponseEntity.ok(updated);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        emergencyContactService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
