package com.medicalcontinuity.medicalcontinuity.controller;

import com.medicalcontinuity.medicalcontinuity.entity.AiSummary;
import com.medicalcontinuity.medicalcontinuity.service.AiSummaryService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/ai-summaries")
@RequiredArgsConstructor
public class AiSummaryController {

    private final AiSummaryService aiSummaryService;

    @PostMapping
    public ResponseEntity<AiSummary> create(@RequestBody AiSummary summary) {
        return ResponseEntity.ok(aiSummaryService.create(summary));
    }

    @GetMapping("/{id}")
    public ResponseEntity<AiSummary> getById(@PathVariable Long id) {
        return aiSummaryService.findById(id)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @GetMapping("/patient/{patientId}")
    public ResponseEntity<List<AiSummary>> getByPatient(@PathVariable Long patientId) {
        return ResponseEntity.ok(aiSummaryService.findByPatientId(patientId));
    }

    @GetMapping("/patient/{patientId}/published")
    public ResponseEntity<List<AiSummary>> getPublishedByPatient(@PathVariable Long patientId) {
        return ResponseEntity.ok(aiSummaryService.findPublishedByPatientId(patientId));
    }

    @PatchMapping("/{id}/publish")
    public ResponseEntity<AiSummary> publish(@PathVariable Long id) {
        return ResponseEntity.ok(aiSummaryService.publish(id));
    }
}