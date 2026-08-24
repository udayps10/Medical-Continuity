package com.medicalcontinuity.medicalcontinuity.controller;

import com.medicalcontinuity.medicalcontinuity.entity.Consent;
import com.medicalcontinuity.medicalcontinuity.service.ConsentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/consents")
public class ConsentController {

    @Autowired
    private ConsentService consentService;

    @GetMapping
    public List<Consent> getAllConsents() {
        return consentService.getAllConsents();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Consent> getConsentById(@PathVariable Long id) {
        return consentService.getConsentById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public Consent createConsent(@RequestBody Consent consent) {
        return consentService.createConsent(consent);
    }

    @PutMapping("/{id}/revoke")
    public ResponseEntity<Consent> revokeConsent(@PathVariable Long id) {
        try {
            return ResponseEntity.ok(consentService.revokeConsent(id));
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteConsent(@PathVariable Long id) {
        consentService.deleteConsent(id);
        return ResponseEntity.noContent().build();
    }
}
