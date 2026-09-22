package com.medicalcontinuity.medicalcontinuity.service;

import com.medicalcontinuity.medicalcontinuity.entity.Encounter;
import com.medicalcontinuity.medicalcontinuity.repository.EncounterRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional
public class EncounterService {

    private final EncounterRepository encounterRepository;

    public Encounter create(Encounter encounter) {
        return encounterRepository.save(encounter);
    }

    @Transactional(readOnly = true)
    public Optional<Encounter> findById(Long id) {
        return encounterRepository.findById(id);
    }

    @Transactional(readOnly = true)
    public List<Encounter> findByPatientId(Long patientId) {
        return encounterRepository.findByPatientId(patientId);
    }

    @Transactional(readOnly = true)
    public List<Encounter> findByHospitalId(Long hospitalId) {
        return encounterRepository.findByHospitalId(hospitalId);
    }

    public Encounter discharge(Long id) {
        Encounter existing = encounterRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Encounter not found: " + id));
        existing.setDischargeTime(LocalDateTime.now());
        return encounterRepository.save(existing);
    }
}