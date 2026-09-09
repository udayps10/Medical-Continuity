package com.medicalcontinuity.medicalcontinuity.controller;

import com.medicalcontinuity.medicalcontinuity.entity.PatientMatch;
import com.medicalcontinuity.medicalcontinuity.service.AiMatchingService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/ai")
public class AiMatchingController {

    private final AiMatchingService aiMatchingService;

    public AiMatchingController(AiMatchingService aiMatchingService) {
        this.aiMatchingService = aiMatchingService;
    }

    @GetMapping("/health")
    public ResponseEntity<Map<String, String>> healthCheck() {
        return ResponseEntity.ok(Map.of("status", "ok", "service", "ai-matching"));
    }

    @GetMapping("/match/{unknownPatientId}")
    public ResponseEntity<Map<String, Object>> getMatches(@PathVariable Long unknownPatientId) {
        Map<String, Object> result = aiMatchingService.getMatchesFromAI(unknownPatientId);
        return ResponseEntity.ok(result);
    }

    @PostMapping("/match/{unknownPatientId}")
    public ResponseEntity<List<PatientMatch>> runMatchAndSave(@PathVariable Long unknownPatientId) {
        List<PatientMatch> matches = aiMatchingService.runMatchAndSave(unknownPatientId);
        return ResponseEntity.ok(matches);
    }
}
