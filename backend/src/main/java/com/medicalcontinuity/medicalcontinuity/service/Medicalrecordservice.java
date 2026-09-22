package com.medicalcontinuity.medicalcontinuity.service;

import com.medicalcontinuity.medicalcontinuity.entity.MedicalRecord;
import com.medicalcontinuity.medicalcontinuity.repository.MedicalRecordRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional
public class MedicalRecordService {

    private final MedicalRecordRepository medicalRecordRepository;

    public MedicalRecord create(MedicalRecord record) {
        return medicalRecordRepository.save(record);
    }

    @Transactional(readOnly = true)
    public Optional<MedicalRecord> findById(Long id) {
        return medicalRecordRepository.findById(id);
    }

    @Transactional(readOnly = true)
    public List<MedicalRecord> findByPatientId(Long patientId) {
        return medicalRecordRepository.findByPatientId(patientId);
    }

    @Transactional(readOnly = true)
    public List<MedicalRecord> findByEncounterId(Long encounterId) {
        return medicalRecordRepository.findByEncounterId(encounterId);
    }

    public MedicalRecord update(Long id, MedicalRecord updated) {
        MedicalRecord existing = medicalRecordRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Medical record not found: " + id));
        existing.setSummary(updated.getSummary());
        existing.setNotes(updated.getNotes());
        return medicalRecordRepository.save(existing);
    }

    public void delete(Long id) {
        medicalRecordRepository.deleteById(id);
    }
}