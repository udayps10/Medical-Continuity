package com.medicalcontinuity.medicalcontinuity.controller;

import com.medicalcontinuity.medicalcontinuity.entity.Patient;
import com.medicalcontinuity.medicalcontinuity.entity.UnknownPatientCase;
import com.medicalcontinuity.medicalcontinuity.service.MinioStorageService;
import com.medicalcontinuity.medicalcontinuity.service.PatientService;
import com.medicalcontinuity.medicalcontinuity.service.UnknownPatientCaseService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/api/photos")
public class PhotoController {

    private final MinioStorageService minioStorageService;
    private final PatientService patientService;
    private final UnknownPatientCaseService unknownPatientCaseService;

    public PhotoController(MinioStorageService minioStorageService,
                           PatientService patientService,
                           UnknownPatientCaseService unknownPatientCaseService) {
        this.minioStorageService = minioStorageService;
        this.patientService = patientService;
        this.unknownPatientCaseService = unknownPatientCaseService;
    }

    @PostMapping("/patients/{patientId}")
    public ResponseEntity<Patient> uploadPatientPhoto(
            @PathVariable Long patientId, @RequestParam("file") MultipartFile file) {
        String objectKey = minioStorageService.uploadPhoto(file, "patients");

        Patient patient = patientService.findById(patientId)
                .orElseThrow(() -> new IllegalArgumentException("Patient not found: " + patientId));
        patient.setPhotoRef(objectKey);
        return ResponseEntity.ok(patientService.update(patientId, patient));
    }

    @PostMapping("/unknown-cases/{caseId}")
    public ResponseEntity<UnknownPatientCase> uploadUnknownCasePhoto(
            @PathVariable Long caseId, @RequestParam("file") MultipartFile file) {
        String objectKey = minioStorageService.uploadPhoto(file, "unknown-cases");

        UnknownPatientCase unknownCase = unknownPatientCaseService.findById(caseId)
                .orElseThrow(() -> new IllegalArgumentException("Unknown case not found: " + caseId));
        unknownCase.setPhotoRef(objectKey);
        return ResponseEntity.ok(unknownPatientCaseService.save(unknownCase));
    }

    @GetMapping("/url")
    public ResponseEntity<String> getPhotoUrl(@RequestParam String objectKey) {
        return ResponseEntity.ok(minioStorageService.getPhotoUrl(objectKey));
    }

    @DeleteMapping
    public ResponseEntity<Void> deletePhoto(@RequestParam String objectKey) {
        minioStorageService.deletePhoto(objectKey);
        return ResponseEntity.noContent().build();
    }
}
