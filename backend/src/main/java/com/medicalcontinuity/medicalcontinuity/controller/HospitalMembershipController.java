package com.medicalcontinuity.medicalcontinuity.controller;

import com.medicalcontinuity.medicalcontinuity.entity.HospitalMembership;
import com.medicalcontinuity.medicalcontinuity.service.HospitalMembershipService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/hospital-memberships")
@RequiredArgsConstructor
public class HospitalMembershipController {

    private final HospitalMembershipService hospitalMembershipService;

    @PostMapping
    public ResponseEntity<HospitalMembership> create(@RequestBody HospitalMembership membership) {
        return ResponseEntity.ok(hospitalMembershipService.create(membership));
    }

    @GetMapping("/{id}")
    public ResponseEntity<HospitalMembership> getById(@PathVariable Long id) {
        return hospitalMembershipService.findById(id)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<List<HospitalMembership>> getByUser(@PathVariable Long userId) {
        return ResponseEntity.ok(hospitalMembershipService.findByUserId(userId));
    }

    @GetMapping("/hospital/{hospitalId}")
    public ResponseEntity<List<HospitalMembership>> getByHospital(@PathVariable Long hospitalId) {
        return ResponseEntity.ok(hospitalMembershipService.findByHospitalId(hospitalId));
    }

    @PatchMapping("/{id}/status")
    public ResponseEntity<HospitalMembership> updateStatus(
            @PathVariable Long id, @RequestParam HospitalMembership.MembershipStatus status) {
        return ResponseEntity.ok(hospitalMembershipService.updateStatus(id, status));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        hospitalMembershipService.delete(id);
        return ResponseEntity.noContent().build();
    }
}