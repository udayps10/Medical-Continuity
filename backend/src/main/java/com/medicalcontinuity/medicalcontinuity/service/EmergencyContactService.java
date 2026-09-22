package com.medicalcontinuity.medicalcontinuity.service;

import com.medicalcontinuity.medicalcontinuity.entity.EmergencyContact;
import com.medicalcontinuity.medicalcontinuity.repository.EmergencyContactRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class EmergencyContactService {

    private final EmergencyContactRepository emergencyContactRepository;

    public EmergencyContact create(EmergencyContact contact) {
        return emergencyContactRepository.save(contact);
    }

    @Transactional(readOnly = true)
    public List<EmergencyContact> findByPatientId(Long patientId) {
        return emergencyContactRepository.findByPatientId(patientId);
    }

    public EmergencyContact update(Long id, EmergencyContact updated) {
        EmergencyContact existing = emergencyContactRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Emergency contact not found: " + id));
        existing.setName(updated.getName());
        existing.setRelationship(updated.getRelationship());
        existing.setPhone(updated.getPhone());
        existing.setPrimary(updated.isPrimary());
        return emergencyContactRepository.save(existing);
    }

    public void delete(Long id) {
        emergencyContactRepository.deleteById(id);
    }
}