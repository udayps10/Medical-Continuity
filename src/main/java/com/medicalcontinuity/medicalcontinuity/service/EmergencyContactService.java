package com.medicalcontinuity.medicalcontinuity.service;

import com.medicalcontinuity.medicalcontinuity.entity.EmergencyContact;
import com.medicalcontinuity.medicalcontinuity.entity.Patient;
import com.medicalcontinuity.medicalcontinuity.exception.ResourceNotFoundException;
import com.medicalcontinuity.medicalcontinuity.repositories.EmergencyContactRepository;
import com.medicalcontinuity.medicalcontinuity.repositories.PatientRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class EmergencyContactService {

    private final EmergencyContactRepository emergencyContactRepository;
    private final PatientRepository patientRepository;

    public EmergencyContactService(EmergencyContactRepository emergencyContactRepository,
                                   PatientRepository patientRepository) {
        this.emergencyContactRepository = emergencyContactRepository;
        this.patientRepository = patientRepository;
    }

    public EmergencyContact create(EmergencyContact contact) {
        Long patientId = contact.getPatient().getId();
        Patient patient = patientRepository.findById(patientId)
                .orElseThrow(() -> new ResourceNotFoundException("Patient not found with id: " + patientId));
        contact.setPatient(patient);
        return emergencyContactRepository.save(contact);
    }

    @Transactional(readOnly = true)
    public EmergencyContact getById(Long id) {
        return emergencyContactRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("EmergencyContact not found with id: " + id));
    }

    @Transactional(readOnly = true)
    public List<EmergencyContact> getAll() {
        return emergencyContactRepository.findAll();
    }

    @Transactional(readOnly = true)
    public List<EmergencyContact> getByPatientId(Long patientId) {
        return emergencyContactRepository.findByPatientId(patientId);
    }

    public EmergencyContact update(Long id, EmergencyContact updated) {
        EmergencyContact existing = getById(id);
        existing.setName(updated.getName());
        existing.setRelationship(updated.getRelationship());
        existing.setPhone(updated.getPhone());
        existing.setAddress(updated.getAddress());
        return emergencyContactRepository.save(existing);
    }

    public void delete(Long id) {
        EmergencyContact contact = getById(id);
        emergencyContactRepository.delete(contact);
    }
}
