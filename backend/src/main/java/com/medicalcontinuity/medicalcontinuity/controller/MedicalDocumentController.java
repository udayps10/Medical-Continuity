package com.medicalcontinuity.medicalcontinuity.controller;

import com.medicalcontinuity.medicalcontinuity.entity.MedicalDocument;
import com.medicalcontinuity.medicalcontinuity.service.MedicalDocumentService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/medical-documents")
public class MedicalDocumentController {

    private final MedicalDocumentService medicalDocumentService;

    public MedicalDocumentController(MedicalDocumentService medicalDocumentService) {
        this.medicalDocumentService = medicalDocumentService;
    }

    @PostMapping
    public ResponseEntity<MedicalDocument> createMedicalDocument(@RequestBody MedicalDocument medicalDocument) {
        MedicalDocument created = medicalDocumentService.createMedicalDocument(medicalDocument);
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }

    @GetMapping("/{id}")
    public ResponseEntity<MedicalDocument> getMedicalDocumentById(@PathVariable Long id) {
        MedicalDocument document = medicalDocumentService.getMedicalDocumentById(id);
        return ResponseEntity.ok(document);
    }

    @GetMapping("/patient/{patientId}")
    public ResponseEntity<List<MedicalDocument>> getDocumentsByPatientId(@PathVariable Long patientId) {
        List<MedicalDocument> documents = medicalDocumentService.getDocumentsByPatientId(patientId);
        return ResponseEntity.ok(documents);
    }

    @GetMapping("/medical-record/{medicalRecordId}")
    public ResponseEntity<List<MedicalDocument>> getDocumentsByMedicalRecordId(@PathVariable Long medicalRecordId) {
        List<MedicalDocument> documents = medicalDocumentService.getDocumentsByMedicalRecordId(medicalRecordId);
        return ResponseEntity.ok(documents);
    }

    @PutMapping("/{id}")
    public ResponseEntity<MedicalDocument> updateMedicalDocument(@PathVariable Long id, @RequestBody MedicalDocument medicalDocument) {
        MedicalDocument updated = medicalDocumentService.updateMedicalDocument(id, medicalDocument);
        return ResponseEntity.ok(updated);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteMedicalDocument(@PathVariable Long id) {
        medicalDocumentService.deleteMedicalDocument(id);
        return ResponseEntity.noContent().build();
    }
}
