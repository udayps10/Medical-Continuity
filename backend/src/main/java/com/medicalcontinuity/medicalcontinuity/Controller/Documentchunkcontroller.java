package com.medicalcontinuity.medicalcontinuity.controller;

import com.medicalcontinuity.medicalcontinuity.entity.DocumentChunk;
import com.medicalcontinuity.medicalcontinuity.service.DocumentChunkService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/document-chunks")
@RequiredArgsConstructor
public class DocumentChunkController {

    private final DocumentChunkService documentChunkService;

    @PostMapping
    public ResponseEntity<DocumentChunk> create(@RequestBody DocumentChunk chunk) {
        return ResponseEntity.ok(documentChunkService.create(chunk));
    }

    @PostMapping("/batch")
    public ResponseEntity<List<DocumentChunk>> createAll(@RequestBody List<DocumentChunk> chunks) {
        return ResponseEntity.ok(documentChunkService.createAll(chunks));
    }

    @GetMapping("/document/{documentId}")
    public ResponseEntity<List<DocumentChunk>> getByDocument(@PathVariable Long documentId) {
        return ResponseEntity.ok(documentChunkService.findByDocumentId(documentId));
    }
}