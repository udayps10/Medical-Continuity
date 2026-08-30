package com.medicalcontinuity.medicalcontinuity.service;

import com.medicalcontinuity.medicalcontinuity.entity.Patient;
import com.medicalcontinuity.medicalcontinuity.repositories.PatientRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class PatientService {

    private final PatientRepository patientRepository;

    public PatientService(PatientRepository patientRepository) {
        this.patientRepository = patientRepository;
    }

    public Patient createPatient(Patient patient) {
        return patientRepository.save(patient);
    }

    @Transactional(readOnly = true)
    public Patient getPatientById(Long id) {
        return patientRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Patient not found with id: " + id));
    }

    @Transactional(readOnly = true)
    public Patient getPatientByMcid(String mcid) {
        return patientRepository.findByMcid(mcid)
                .orElseThrow(() -> new RuntimeException("Patient not found with MCID: " + mcid));
    }

    @Transactional(readOnly = true)
    public List<Patient> getAllPatients() {
        return patientRepository.findAll();
    }

    public Patient updatePatient(Long id, Patient updatedPatient) {
        Patient existing = getPatientById(id);
        existing.setName(updatedPatient.getName());
        existing.setDateOfBirth(updatedPatient.getDateOfBirth());
        existing.setGender(updatedPatient.getGender());
        existing.setPhone(updatedPatient.getPhone());
        existing.setAddress(updatedPatient.getAddress());
        existing.setVillage(updatedPatient.getVillage());
        return patientRepository.save(existing);
    }

    public void deletePatient(Long id) {
        Patient patient = getPatientById(id);
        patientRepository.delete(patient);
    }
}
