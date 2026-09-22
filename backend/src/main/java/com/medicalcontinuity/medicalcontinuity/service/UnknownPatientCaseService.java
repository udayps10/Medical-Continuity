package com.medicalcontinuity.medicalcontinuity.service;

import com.medicalcontinuity.medicalcontinuity.entity.UnknownPatientCase;
import com.medicalcontinuity.medicalcontinuity.repository.PatientRepository;
import com.medicalcontinuity.medicalcontinuity.repository.UnknownPatientCaseRepository;
import com.medicalcontinuity.medicalcontinuity.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional
public class UnknownPatientCaseService {

    private final UnknownPatientCaseRepository unknownPatientCaseRepository;
    private final PatientRepository patientRepository;
    private final UserRepository userRepository;

    public UnknownPatientCase create(UnknownPatientCase unknownCase) {
        if (unknownCase.getStatus() == null) {
            unknownCase.setStatus(UnknownPatientCase.Status.OPEN);
        }
        return unknownPatientCaseRepository.save(unknownCase);
    }

    @Transactional(readOnly = true)
    public Optional<UnknownPatientCase> findById(Long id) {
        return unknownPatientCaseRepository.findById(id);
    }

    @Transactional(readOnly = true)
    public Optional<UnknownPatientCase> findByTemporaryId(String temporaryId) {
        return unknownPatientCaseRepository.findByTemporaryId(temporaryId);
    }

    @Transactional(readOnly = true)
    public List<UnknownPatientCase> findOpenCases() {
        return unknownPatientCaseRepository.findByStatus(UnknownPatientCase.Status.OPEN);
    }

    public UnknownPatientCase resolve(Long id, Long resolvedPatientId, Long resolvedByUserId) {
        UnknownPatientCase existing = unknownPatientCaseRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Unknown patient case not found: " + id));
        existing.setStatus(UnknownPatientCase.Status.RESOLVED);
        existing.setResolvedPatient(patientRepository.findById(resolvedPatientId)
                .orElseThrow(() -> new IllegalArgumentException("Patient not found: " + resolvedPatientId)));
        existing.setResolvedByUser(userRepository.findById(resolvedByUserId)
                .orElseThrow(() -> new IllegalArgumentException("User not found: " + resolvedByUserId)));
        existing.setResolvedAt(LocalDateTime.now());
        return unknownPatientCaseRepository.save(existing);
    }
}