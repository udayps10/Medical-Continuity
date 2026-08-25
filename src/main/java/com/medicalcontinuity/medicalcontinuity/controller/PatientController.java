package com.medicalcontinuity.medicalcontinuity.controller;

import com.medicalcontinuity.medicalcontinuity.dto.UpdatePatientRequest;
import com.medicalcontinuity.medicalcontinuity.entity.*;
import com.medicalcontinuity.medicalcontinuity.service.PatientService;
import com.medicalcontinuity.medicalcontinuity.service.AuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/patient")
public class PatientController {

    @Autowired
    private PatientService patientService;

    @Autowired
    private AuthService authService;

    @GetMapping("/me")
    public ResponseEntity<Patient> getMyProfile(@RequestHeader("Authorization") String auth) {
        Long userId = authService.getUserIdFromToken(auth.replace("Bearer ", ""));
        return patientService.getPatientByUserId(userId)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PutMapping("/me")
    public ResponseEntity<Patient> updateMyProfile(@RequestHeader("Authorization") String auth, @RequestBody UpdatePatientRequest req) {
        Long userId = authService.getUserIdFromToken(auth.replace("Bearer ", ""));
        Patient patient = patientService.getPatientByUserId(userId).orElseThrow(() -> new RuntimeException("Patient not found"));
        return ResponseEntity.ok(patientService.updatePatient(patient.getId(), req));
    }

    @GetMapping("/visits")
    public ResponseEntity<List<Visit>> getMyVisits(@RequestHeader("Authorization") String auth) {
        Long userId = authService.getUserIdFromToken(auth.replace("Bearer ", ""));
        Patient patient = patientService.getPatientByUserId(userId).orElseThrow(() -> new RuntimeException("Patient not found"));
        return ResponseEntity.ok(patientService.getVisits(patient.getId()));
    }

    @PostMapping("/consent")
    public ResponseEntity<Patient> grantConsent(@RequestHeader("Authorization") String auth) {
        Long userId = authService.getUserIdFromToken(auth.replace("Bearer ", ""));
        Patient patient = patientService.getPatientByUserId(userId).orElseThrow(() -> new RuntimeException("Patient not found"));
        return ResponseEntity.ok(patientService.grantConsent(patient.getId()));
    }

    @DeleteMapping("/consent")
    public ResponseEntity<Patient> revokeConsent(@RequestHeader("Authorization") String auth) {
        Long userId = authService.getUserIdFromToken(auth.replace("Bearer ", ""));
        Patient patient = patientService.getPatientByUserId(userId).orElseThrow(() -> new RuntimeException("Patient not found"));
        return ResponseEntity.ok(patientService.revokeConsent(patient.getId()));
    }

    @GetMapping("/access-log")
    public ResponseEntity<List<AccessLog>> getAccessLog(@RequestHeader("Authorization") String auth) {
        Long userId = authService.getUserIdFromToken(auth.replace("Bearer ", ""));
        Patient patient = patientService.getPatientByUserId(userId).orElseThrow(() -> new RuntimeException("Patient not found"));
        return ResponseEntity.ok(patientService.getAccessLogs(patient.getId()));
    }
}
