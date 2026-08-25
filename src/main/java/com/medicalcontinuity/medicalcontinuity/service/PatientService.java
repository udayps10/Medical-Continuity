package com.medicalcontinuity.medicalcontinuity.service;

import com.medicalcontinuity.medicalcontinuity.dto.UpdatePatientRequest;
import com.medicalcontinuity.medicalcontinuity.entity.*;
import com.medicalcontinuity.medicalcontinuity.repositories.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class PatientService {

    @Autowired
    private PatientRepository patientRepository;

    @Autowired
    private VisitRepository visitRepository;

    @Autowired
    private RecordRepository recordRepository;

    @Autowired
    private AccessLogRepository accessLogRepository;

    public Optional<Patient> getPatientByUserId(Long userId) {
        return patientRepository.findByUserId(userId);
    }

    public Patient getPatientById(Long id) {
        return patientRepository.findById(id).orElseThrow(() -> new RuntimeException("Patient not found"));
    }

    public Patient updatePatient(Long id, UpdatePatientRequest req) {
        Patient patient = patientRepository.findById(id).orElseThrow(() -> new RuntimeException("Patient not found"));
        if (req.getAllergies() != null) {
            patient.setAllergies(req.getAllergies());
        }
        return patientRepository.save(patient);
    }

    public List<Visit> getVisits(Long patientId) {
        return visitRepository.findByPatientId(patientId);
    }

    public Patient grantConsent(Long patientId) {
        Patient patient = patientRepository.findById(patientId).orElseThrow(() -> new RuntimeException("Patient not found"));
        patient.setConsentGiven(true);
        return patientRepository.save(patient);
    }

    public Patient revokeConsent(Long patientId) {
        Patient patient = patientRepository.findById(patientId).orElseThrow(() -> new RuntimeException("Patient not found"));
        patient.setConsentGiven(false);
        return patientRepository.save(patient);
    }

    public List<AccessLog> getAccessLogs(Long patientId) {
        return accessLogRepository.findAll();
    }
}
