package com.medicalcontinuity.medicalcontinuity.service;

import com.medicalcontinuity.medicalcontinuity.entity.*;
import com.medicalcontinuity.medicalcontinuity.repositories.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class ConsentService {

    @Autowired
    private ConsentRepository consentRepository;

    @Autowired
    private AccessLogRepository accessLogRepository;

    public List<Consent> getAllConsents() {
        return consentRepository.findAll();
    }

    public Optional<Consent> getConsentById(Long id) {
        return consentRepository.findById(id);
    }

    public List<Consent> getByPatientId(Long patientId) {
        return consentRepository.findByPatientId(patientId);
    }

    public Consent createConsent(Consent consent) {
        consent.setGrantedAt(LocalDateTime.now());
        consent.setActive(true);
        return consentRepository.save(consent);
    }

    public Consent revokeConsent(Long id) {
        Consent consent = consentRepository.findById(id).orElseThrow(() -> new RuntimeException("Consent not found"));
        consent.setRevokedAt(LocalDateTime.now());
        consent.setActive(false);
        return consentRepository.save(consent);
    }

    public void deleteConsent(Long id) {
        consentRepository.deleteById(id);
    }
}
