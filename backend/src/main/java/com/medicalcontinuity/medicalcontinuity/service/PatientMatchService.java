package com.medicalcontinuity.medicalcontinuity.service;

import com.medicalcontinuity.medicalcontinuity.entity.Patient;
import com.medicalcontinuity.medicalcontinuity.entity.PatientMatch;
import com.medicalcontinuity.medicalcontinuity.entity.UnknownPatient;
import com.medicalcontinuity.medicalcontinuity.enums.PatientMatchStatus;
import com.medicalcontinuity.medicalcontinuity.exception.ResourceNotFoundException;
import com.medicalcontinuity.medicalcontinuity.repositories.PatientRepository;
import com.medicalcontinuity.medicalcontinuity.repositories.PatientMatchRepository;
import com.medicalcontinuity.medicalcontinuity.repositories.UnknownPatientRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class PatientMatchService {

    private final PatientMatchRepository patientMatchRepository;
    private final UnknownPatientRepository unknownPatientRepository;
    private final PatientRepository patientRepository;

    public PatientMatchService(PatientMatchRepository patientMatchRepository,
                               UnknownPatientRepository unknownPatientRepository,
                               PatientRepository patientRepository) {
        this.patientMatchRepository = patientMatchRepository;
        this.unknownPatientRepository = unknownPatientRepository;
        this.patientRepository = patientRepository;
    }

    public PatientMatch createPatientMatch(PatientMatch patientMatch) {
        patientMatch.setStatus(PatientMatchStatus.PENDING);
        return patientMatchRepository.save(patientMatch);
    }

    @Transactional(readOnly = true)
    public PatientMatch getPatientMatchById(Long id) {
        return patientMatchRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("PatientMatch not found with id: " + id));
    }

    @Transactional(readOnly = true)
    public List<PatientMatch> getMatchesByUnknownPatientId(Long unknownPatientId) {
        return patientMatchRepository.findByUnknownPatientId(unknownPatientId);
    }

    public PatientMatch updateStatus(Long id, PatientMatchStatus status, String reviewedBy) {
        PatientMatch existing = getPatientMatchById(id);
        existing.setStatus(status);
        existing.setReviewedBy(reviewedBy);
        return patientMatchRepository.save(existing);
    }

    public void deletePatientMatch(Long id) {
        PatientMatch patientMatch = getPatientMatchById(id);
        patientMatchRepository.delete(patientMatch);
    }
}
