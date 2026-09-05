package com.medicalcontinuity.medicalcontinuity.service;

import com.medicalcontinuity.medicalcontinuity.entity.MedicalDocument;
import com.medicalcontinuity.medicalcontinuity.enums.ProcessingStatus;
import com.medicalcontinuity.medicalcontinuity.exception.ResourceNotFoundException;
import com.medicalcontinuity.medicalcontinuity.repositories.MedicalDocumentRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class MedicalDocumentService {

    private final MedicalDocumentRepository medicalDocumentRepository;

    public MedicalDocumentService(MedicalDocumentRepository medicalDocumentRepository) {
        this.medicalDocumentRepository = medicalDocumentRepository;
    }

    public MedicalDocument createMedicalDocument(MedicalDocument medicalDocument) {
        medicalDocument.setProcessingStatus(ProcessingStatus.PENDING);
        return medicalDocumentRepository.save(medicalDocument);
    }

    @Transactional(readOnly = true)
    public MedicalDocument getMedicalDocumentById(Long id) {
        return medicalDocumentRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("MedicalDocument not found with id: " + id));
    }

    @Transactional(readOnly = true)
    public List<MedicalDocument> getDocumentsByPatientId(Long patientId) {
        return medicalDocumentRepository.findByPatientId(patientId);
    }

    @Transactional(readOnly = true)
    public List<MedicalDocument> getDocumentsByMedicalRecordId(Long medicalRecordId) {
        return medicalDocumentRepository.findByMedicalRecordId(medicalRecordId);
    }

    public MedicalDocument updateMedicalDocument(Long id, MedicalDocument updated) {
        MedicalDocument existing = getMedicalDocumentById(id);
        existing.setDocumentType(updated.getDocumentType());
        existing.setFileName(updated.getFileName());
        existing.setMimeType(updated.getMimeType());
        existing.setFileSize(updated.getFileSize());
        existing.setProcessingStatus(updated.getProcessingStatus());
        existing.setProcessedAt(updated.getProcessedAt());
        return medicalDocumentRepository.save(existing);
    }

    public void deleteMedicalDocument(Long id) {
        MedicalDocument medicalDocument = getMedicalDocumentById(id);
        medicalDocumentRepository.delete(medicalDocument);
    }
}
