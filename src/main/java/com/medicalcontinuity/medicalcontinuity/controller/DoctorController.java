package com.medicalcontinuity.medicalcontinuity.controller;

import com.medicalcontinuity.medicalcontinuity.dto.*;
import com.medicalcontinuity.medicalcontinuity.entity.*;
import com.medicalcontinuity.medicalcontinuity.service.DoctorService;
import com.medicalcontinuity.medicalcontinuity.service.AuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/doctor")
public class DoctorController {

    @Autowired
    private DoctorService doctorService;

    @Autowired
    private AuthService authService;

    @GetMapping("/patient/{patientId}/summary")
    public ResponseEntity<Map<String, Object>> getPatientSummary(
            @RequestHeader("Authorization") String auth,
            @PathVariable Long patientId) {
        Long userId = authService.getUserIdFromToken(auth.replace("Bearer ", ""));
        Doctor doctor = doctorService.getDoctorByUserId(userId).orElseThrow(() -> new RuntimeException("Doctor not found"));
        return ResponseEntity.ok(doctorService.getPatientSummary(patientId, doctor.getId()));
    }

    @PostMapping("/visit")
    public ResponseEntity<Visit> createVisit(@RequestBody CreateVisitRequest req) {
        return ResponseEntity.ok(doctorService.createVisit(req));
    }

    @PostMapping("/record")
    public ResponseEntity<Record> addRecord(@RequestBody CreateRecordRequest req) {
        return ResponseEntity.ok(doctorService.addRecord(req));
    }

    @PutMapping("/record/{id}")
    public ResponseEntity<Record> updateRecord(@PathVariable Long id, @RequestBody UpdateRecordRequest req) {
        return ResponseEntity.ok(doctorService.updateRecord(id, req));
    }
}
