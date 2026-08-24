package com.medicalcontinuity.medicalcontinuity.service;

import com.medicalcontinuity.medicalcontinuity.entity.Consent;
import com.medicalcontinuity.medicalcontinuity.repositories.ConsentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class ConsentService {

    @Autowired
    private ConsentRepository consentRepository;

    public List<Consent> getAllConsents() {
        return consentRepository.findAll();
    }

    public Optional<Consent> getConsentById(Long id) {
        return consentRepository.findById(id);
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
