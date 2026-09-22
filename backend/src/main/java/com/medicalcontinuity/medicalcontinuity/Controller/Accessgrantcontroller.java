package com.medicalcontinuity.medicalcontinuity.controller;

import com.medicalcontinuity.medicalcontinuity.entity.AccessGrant;
import com.medicalcontinuity.medicalcontinuity.service.AccessGrantService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/access-grants")
@RequiredArgsConstructor
public class AccessGrantController {

    private final AccessGrantService accessGrantService;

    @PostMapping
    public ResponseEntity<AccessGrant> create(@RequestBody AccessGrant grant) {
        return ResponseEntity.ok(accessGrantService.create(grant));
    }

    @GetMapping("/{id}")
    public ResponseEntity<AccessGrant> getById(@PathVariable Long id) {
        return accessGrantService.findById(id)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @GetMapping("/patient/{patientId}")
    public ResponseEntity<List<AccessGrant>> getByPatient(@PathVariable Long patientId) {
        return ResponseEntity.ok(accessGrantService.findByPatientId(patientId));
    }

    @GetMapping("/patient/{patientId}/active")
    public ResponseEntity<List<AccessGrant>> getActiveByPatient(@PathVariable Long patientId) {
        return ResponseEntity.ok(accessGrantService.findActiveByPatientId(patientId));
    }

    @PatchMapping("/{id}/revoke")
    public ResponseEntity<AccessGrant> revoke(@PathVariable Long id, @RequestParam Long revokedByUserId) {
        return ResponseEntity.ok(accessGrantService.revoke(id, revokedByUserId));
    }
}