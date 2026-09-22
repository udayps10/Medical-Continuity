package com.medicalcontinuity.medicalcontinuity.controller;

import com.medicalcontinuity.medicalcontinuity.entity.MedicalDocument;
import com.medicalcontinuity.medicalcontinuity.service.MedicalDocumentService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/medical-documents")
@RequiredArgsConstructor
public class MedicalDocumentController {

    private final MedicalDocumentService medicalDocumentService;

    @PostMapping
    public ResponseEntity<MedicalDocument> upload(@RequestBody MedicalDocument document) {
        return ResponseEntity.ok(medicalDocumentService.upload(document));
    }

    @GetMapping("/{id}")
    public ResponseEntity<MedicalDocument> getById(@PathVariable Long id) {
        return medicalDocumentService.findById(id)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @GetMapping("/patient/{patientId}")
    public ResponseEntity<List<MedicalDocument>> getByPatient(@PathVariable Long patientId) {
        return ResponseEntity.ok(medicalDocumentService.findByPatientId(patientId));
    }

    @GetMapping("/pending")
    public ResponseEntity<List<MedicalDocument>> getPending() {
        return ResponseEntity.ok(medicalDocumentService.findPendingProcessing());
    }

    @PatchMapping("/{id}/processed")
    public ResponseEntity<MedicalDocument> markProcessed(@PathVariable Long id) {
        return ResponseEntity.ok(medicalDocumentService.markProcessed(id));
    }

    @PatchMapping("/{id}/failed")
    public ResponseEntity<MedicalDocument> markFailed(@PathVariable Long id, @RequestParam String error) {
        return ResponseEntity.ok(medicalDocumentService.markFailed(id, error));
    }
}