package com.medicalcontinuity.medicalcontinuity.controller;

import com.medicalcontinuity.medicalcontinuity.dto.AddDoctorRequest;
import com.medicalcontinuity.medicalcontinuity.dto.CreateHospitalRequest;
import com.medicalcontinuity.medicalcontinuity.entity.*;
import com.medicalcontinuity.medicalcontinuity.service.HospitalService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/hospital")
public class HospitalController {

    @Autowired
    private HospitalService hospitalService;

    @PostMapping
    public ResponseEntity<Hospital> createHospital(@RequestBody CreateHospitalRequest req) {
        return ResponseEntity.ok(hospitalService.createHospital(req.getName(), req.getCity()));
    }

    @GetMapping
    public ResponseEntity<List<Hospital>> getAllHospitals() {
        return ResponseEntity.ok(hospitalService.getAllHospitals());
    }

    @PostMapping("/doctor")
    public ResponseEntity<Doctor> addDoctor(@RequestBody AddDoctorRequest req) {
        return ResponseEntity.ok(hospitalService.addDoctor(req.getUserId(), req.getHospitalId(), req.getSpecialization()));
    }

    @GetMapping("/{hospitalId}/doctors")
    public ResponseEntity<List<Doctor>> getDoctors(@PathVariable Long hospitalId) {
        return ResponseEntity.ok(hospitalService.getDoctors(hospitalId));
    }

    @GetMapping("/{hospitalId}/visits")
    public ResponseEntity<List<Visit>> getVisits(@PathVariable Long hospitalId) {
        return ResponseEntity.ok(hospitalService.getVisits(hospitalId));
    }
}
