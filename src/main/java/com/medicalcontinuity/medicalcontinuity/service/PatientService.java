package com.medicalcontinuity.medicalcontinuity.service;

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
    private ConsentRepository consentRepository;

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

    public List<Consent> getConsents(Long patientId) {
        return consentRepository.findByPatientId(patientId);
    }

    public Consent grantConsent(Long patientId, Long hospitalId) {
        Patient patient = patientRepository.findById(patientId).orElseThrow(() -> new RuntimeException("Patient not found"));
        Hospital hospital = new Hospital();
        hospital.setId(hospitalId);
        Optional<Consent> existing = consentRepository.findByPatientIdAndHospitalId(patientId, hospitalId);
        if (existing.isPresent()) {
            Consent c = existing.get();
            c.setActive(true);
            c.setRevokedAt(null);
            return consentRepository.save(c);
        }
        Consent consent = new Consent();
        consent.setPatient(patient);
        consent.setHospital(hospital);
        return consentRepository.save(consent);
    }

    public Consent revokeConsent(Long patientId, Long hospitalId) {
        Consent consent = consentRepository.findByPatientIdAndHospitalId(patientId, hospitalId)
                .orElseThrow(() -> new RuntimeException("Consent not found"));
        consent.setActive(false);
        consent.setRevokedAt(java.time.LocalDateTime.now());
        return consentRepository.save(consent);
    }

    public List<AccessLog> getAccessLogs(Long patientId) {
        List<Consent> consents = consentRepository.findByPatientId(patientId);
        return consents.stream()
                .flatMap(c -> accessLogRepository.findByConsentId(c.getId()).stream())
                .toList();
    }
}
