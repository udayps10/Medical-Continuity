package com.medicalcontinuity.medicalcontinuity.service;

import com.medicalcontinuity.medicalcontinuity.entity.Patient;
import com.medicalcontinuity.medicalcontinuity.entity.UnknownPatient;
import com.medicalcontinuity.medicalcontinuity.enums.UnknownPatientStatus;
import com.medicalcontinuity.medicalcontinuity.exception.ResourceNotFoundException;
import com.medicalcontinuity.medicalcontinuity.repositories.PatientRepository;
import com.medicalcontinuity.medicalcontinuity.repositories.UnknownPatientRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

@Service
@Transactional
public class UnknownPatientService {

    private final UnknownPatientRepository unknownPatientRepository;
    private final PatientRepository patientRepository;

    public UnknownPatientService(UnknownPatientRepository unknownPatientRepository,
                                 PatientRepository patientRepository) {
        this.unknownPatientRepository = unknownPatientRepository;
        this.patientRepository = patientRepository;
    }

    public UnknownPatient createUnknownPatient(UnknownPatient unknownPatient) {
        unknownPatient.setTemporaryId(generateTemporaryId());
        unknownPatient.setStatus(UnknownPatientStatus.UNIDENTIFIED);
        return unknownPatientRepository.save(unknownPatient);
    }

    @Transactional(readOnly = true)
    public UnknownPatient getUnknownPatientById(Long id) {
        return unknownPatientRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("UnknownPatient not found with id: " + id));
    }

    @Transactional(readOnly = true)
    public UnknownPatient getUnknownPatientByTemporaryId(String temporaryId) {
        return unknownPatientRepository.findByTemporaryId(temporaryId)
                .orElseThrow(() -> new ResourceNotFoundException("UnknownPatient not found with temporaryId: " + temporaryId));
    }

    @Transactional(readOnly = true)
    public List<UnknownPatient> getAllUnknownPatients() {
        return unknownPatientRepository.findAll();
    }

    public UnknownPatient updateUnknownPatient(Long id, UnknownPatient updated) {
        UnknownPatient existing = getUnknownPatientById(id);
        existing.setApproximateAge(updated.getApproximateAge());
        existing.setGender(updated.getGender());
        existing.setLocation(updated.getLocation());
        existing.setDescription(updated.getDescription());
        existing.setStatus(updated.getStatus());
        return unknownPatientRepository.save(existing);
    }

    public void deleteUnknownPatient(Long id) {
        UnknownPatient unknownPatient = getUnknownPatientById(id);
        unknownPatientRepository.delete(unknownPatient);
    }

    public UnknownPatient resolveUnknownPatient(Long unknownPatientId, Long resolvedPatientId) {
        UnknownPatient unknownPatient = getUnknownPatientById(unknownPatientId);
        Patient resolvedPatient = patientRepository.findById(resolvedPatientId)
                .orElseThrow(() -> new ResourceNotFoundException("Patient not found with id: " + resolvedPatientId));
        unknownPatient.setResolvedPatient(resolvedPatient);
        unknownPatient.setStatus(UnknownPatientStatus.IDENTIFIED);
        return unknownPatientRepository.save(unknownPatient);
    }

    private String generateTemporaryId() {
        String temporaryId;
        do {
            int number = ThreadLocalRandom.current().nextInt(10000, 99999);
            temporaryId = "TEMP-" + number;
        } while (unknownPatientRepository.existsByTemporaryId(temporaryId));
        return temporaryId;
    }
}
