package com.medicalcontinuity.medicalcontinuity.controller;

import com.medicalcontinuity.medicalcontinuity.entity.Observation;
import com.medicalcontinuity.medicalcontinuity.service.ObservationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/observations")
@RequiredArgsConstructor
public class ObservationController {

    private final ObservationService observationService;

    @PostMapping
    public ResponseEntity<Observation> create(@RequestBody Observation observation) {
        return ResponseEntity.status(HttpStatus.CREATED).body(observationService.create(observation));
    }

    @GetMapping("/encounter/{encounterId}")
    public ResponseEntity<List<Observation>> getByEncounter(@PathVariable Long encounterId) {
        return ResponseEntity.ok(observationService.findByEncounterId(encounterId));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        observationService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
