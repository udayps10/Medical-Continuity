package com.medicalcontinuity.medicalcontinuity.controller;

import com.medicalcontinuity.medicalcontinuity.entity.MedicalDocument;
import com.medicalcontinuity.medicalcontinuity.enums.DocumentType;
import com.medicalcontinuity.medicalcontinuity.enums.ProcessingStatus;
import com.medicalcontinuity.medicalcontinuity.service.FileStorageService;
import com.medicalcontinuity.medicalcontinuity.service.MedicalDocumentService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/medical-documents")
public class MedicalDocumentController {

    private final MedicalDocumentService medicalDocumentService;
    private final FileStorageService fileStorageService;

    public MedicalDocumentController(MedicalDocumentService medicalDocumentService,
                                     FileStorageService fileStorageService) {
        this.medicalDocumentService = medicalDocumentService;
        this.fileStorageService = fileStorageService;
    }

    @PostMapping("/upload")
    public ResponseEntity<?> uploadDocument(
            @RequestParam("file") MultipartFile file,
            @RequestParam("patientId") Long patientId,
            @RequestParam(value = "medicalRecordId", required = false) Long medicalRecordId,
            @RequestParam("documentType") DocumentType documentType,
            Authentication authentication) {

        try {
            String uploadedBy = authentication != null ? authentication.getName() : "system";

            String storedPath = fileStorageService.storeFile(file, patientId);

            MedicalDocument doc = new MedicalDocument();
            doc.setFileName(file.getOriginalFilename());
            doc.setMimeType(file.getContentType());
            doc.setFileSize(file.getSize());
            doc.setDocumentType(documentType);
            doc.setProcessingStatus(ProcessingStatus.PENDING);
            doc.setUploadedBy(uploadedBy);
            doc.setStoredFilePath(storedPath);

            MedicalDocument created = medicalDocumentService.createMedicalDocument(doc, patientId, medicalRecordId);

            return ResponseEntity.status(HttpStatus.CREATED).body(Map.of(
                    "id", created.getId(),
                    "fileName", created.getFileName(),
                    "uploadedBy", created.getUploadedBy(),
                    "status", created.getProcessingStatus().name()
            ));

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("error", "Upload failed: " + e.getMessage()));
        }
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
