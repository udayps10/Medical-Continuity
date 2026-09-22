package com.medicalcontinuity.medicalcontinuity.service;

import com.medicalcontinuity.medicalcontinuity.entity.MedicalDocument;
import com.medicalcontinuity.medicalcontinuity.repository.MedicalDocumentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional
public class MedicalDocumentService {

    private final MedicalDocumentRepository medicalDocumentRepository;

    public MedicalDocument upload(MedicalDocument document) {
        if (document.getProcessingStatus() == null) {
            document.setProcessingStatus(MedicalDocument.ProcessingStatus.PENDING);
        }
        return medicalDocumentRepository.save(document);
    }

    @Transactional(readOnly = true)
    public Optional<MedicalDocument> findById(Long id) {
        return medicalDocumentRepository.findById(id);
    }

    @Transactional(readOnly = true)
    public List<MedicalDocument> findByPatientId(Long patientId) {
        return medicalDocumentRepository.findByPatientId(patientId);
    }

    @Transactional(readOnly = true)
    public List<MedicalDocument> findPendingProcessing() {
        return medicalDocumentRepository.findByProcessingStatus(MedicalDocument.ProcessingStatus.PENDING);
    }

    public MedicalDocument markProcessed(Long id) {
        MedicalDocument existing = medicalDocumentRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Document not found: " + id));
        existing.setProcessingStatus(MedicalDocument.ProcessingStatus.COMPLETED);
        existing.setProcessedAt(LocalDateTime.now());
        return medicalDocumentRepository.save(existing);
    }

    public MedicalDocument markFailed(Long id, String error) {
        MedicalDocument existing = medicalDocumentRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Document not found: " + id));
        existing.setProcessingStatus(MedicalDocument.ProcessingStatus.FAILED);
        existing.setProcessingError(error);
        existing.setProcessedAt(LocalDateTime.now());
        return medicalDocumentRepository.save(existing);
    }
}