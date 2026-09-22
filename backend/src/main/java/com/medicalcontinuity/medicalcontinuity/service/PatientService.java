package com.medicalcontinuity.medicalcontinuity.service;

import com.medicalcontinuity.medicalcontinuity.entity.Patient;
import com.medicalcontinuity.medicalcontinuity.repository.PatientRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional
public class PatientService {

    private final PatientRepository patientRepository;

    public Patient create(Patient patient) {
        return patientRepository.save(patient);
    }

    @Transactional(readOnly = true)
    public Optional<Patient> findById(Long id) {
        return patientRepository.findById(id);
    }

    @Transactional(readOnly = true)
    public Optional<Patient> findByMcid(String mcid) {
        return patientRepository.findByMcid(mcid);
    }

    @Transactional(readOnly = true)
    public List<Patient> findAll() {
        return patientRepository.findAll();
    }

    public Patient update(Long id, Patient updated) {
        Patient existing = patientRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Patient not found: " + id));
        existing.setName(updated.getName());
        existing.setDob(updated.getDob());
        existing.setGender(updated.getGender());
        existing.setPhone(updated.getPhone());
        existing.setVillage(updated.getVillage());
        existing.setDistrict(updated.getDistrict());
        existing.setBloodGroup(updated.getBloodGroup());
        existing.setAllergies(updated.getAllergies());
        existing.setPhotoRef(updated.getPhotoRef());
        return patientRepository.save(existing);
    }

    public Patient mergeInto(Long sourcePatientId, Long targetPatientId) {
        Patient source = patientRepository.findById(sourcePatientId)
                .orElseThrow(() -> new IllegalArgumentException("Patient not found: " + sourcePatientId));
        Patient target = patientRepository.findById(targetPatientId)
                .orElseThrow(() -> new IllegalArgumentException("Patient not found: " + targetPatientId));
        source.setMergedIntoPatient(target);
        source.setIdentityStatus(Patient.IdentityStatus.MERGED);
        return patientRepository.save(source);
    }

    public void delete(Long id) {
        patientRepository.deleteById(id);
    }
}