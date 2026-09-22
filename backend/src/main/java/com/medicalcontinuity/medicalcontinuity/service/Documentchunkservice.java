package com.medicalcontinuity.medicalcontinuity.service;

import com.medicalcontinuity.medicalcontinuity.entity.DocumentChunk;
import com.medicalcontinuity.medicalcontinuity.repository.DocumentChunkRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class DocumentChunkService {

    private final DocumentChunkRepository documentChunkRepository;

    public DocumentChunk create(DocumentChunk chunk) {
        return documentChunkRepository.save(chunk);
    }

    public List<DocumentChunk> createAll(List<DocumentChunk> chunks) {
        return documentChunkRepository.saveAll(chunks);
    }

    @Transactional(readOnly = true)
    public List<DocumentChunk> findByDocumentId(Long documentId) {
        return documentChunkRepository.findByDocumentIdOrderByChunkIndex(documentId);
    }
}