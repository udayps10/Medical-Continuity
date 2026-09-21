package com.drn.repository;

import com.drn.entity.MedicalDocument;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface MedicalDocumentRepository extends JpaRepository<MedicalDocument, Long> {
    List<MedicalDocument> findByPatientId(Long patientId);
    List<MedicalDocument> findByMedicalRecordId(Long medicalRecordId);
    Optional<MedicalDocument> findByContentHash(String contentHash);
    List<MedicalDocument> findByProcessingStatus(MedicalDocument.ProcessingStatus status);
}