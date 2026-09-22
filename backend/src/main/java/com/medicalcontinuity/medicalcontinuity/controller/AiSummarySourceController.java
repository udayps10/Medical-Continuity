package com.medicalcontinuity.medicalcontinuity.controller;

import com.medicalcontinuity.medicalcontinuity.entity.AiSummarySource;
import com.medicalcontinuity.medicalcontinuity.service.AiSummarySourceService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/ai-summary-sources")
@RequiredArgsConstructor
public class AiSummarySourceController {

    private final AiSummarySourceService aiSummarySourceService;

    @PostMapping
    public ResponseEntity<AiSummarySource> create(@RequestBody AiSummarySource source) {
        return ResponseEntity.ok(aiSummarySourceService.create(source));
    }

    @GetMapping("/summary/{summaryId}")
    public ResponseEntity<List<AiSummarySource>> getBySummary(@PathVariable Long summaryId) {
        return ResponseEntity.ok(aiSummarySourceService.findBySummaryId(summaryId));
    }
}