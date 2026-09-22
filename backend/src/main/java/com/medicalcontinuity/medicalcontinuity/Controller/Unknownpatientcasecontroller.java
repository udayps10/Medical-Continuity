package com.medicalcontinuity.medicalcontinuity.controller;

import com.medicalcontinuity.medicalcontinuity.entity.UnknownPatientCase;
import com.medicalcontinuity.medicalcontinuity.service.UnknownPatientCaseService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/unknown-patient-cases")
@RequiredArgsConstructor
public class UnknownPatientCaseController {

    private final UnknownPatientCaseService unknownPatientCaseService;

    @PostMapping
    public ResponseEntity<UnknownPatientCase> create(@RequestBody UnknownPatientCase unknownCase) {
        return ResponseEntity.ok(unknownPatientCaseService.create(unknownCase));
    }

    @GetMapping("/{id}")
    public ResponseEntity<UnknownPatientCase> getById(@PathVariable Long id) {
        return unknownPatientCaseService.findById(id)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @GetMapping("/temporary/{temporaryId}")
    public ResponseEntity<UnknownPatientCase> getByTemporaryId(@PathVariable String temporaryId) {
        return unknownPatientCaseService.findByTemporaryId(temporaryId)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @GetMapping("/open")
    public ResponseEntity<List<UnknownPatientCase>> getOpenCases() {
        return ResponseEntity.ok(unknownPatientCaseService.findOpenCases());
    }

    @PatchMapping("/{id}/resolve")
    public ResponseEntity<UnknownPatientCase> resolve(
            @PathVariable Long id,
            @RequestParam Long resolvedPatientId,
            @RequestParam Long resolvedByUserId) {
        return ResponseEntity.ok(unknownPatientCaseService.resolve(id, resolvedPatientId, resolvedByUserId));
    }
}