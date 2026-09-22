package com.medicalcontinuity.medicalcontinuity.service;

import com.medicalcontinuity.medicalcontinuity.entity.Observation;
import com.medicalcontinuity.medicalcontinuity.repository.ObservationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class ObservationService {

    private final ObservationRepository observationRepository;

    public Observation create(Observation observation) {
        return observationRepository.save(observation);
    }

    @Transactional(readOnly = true)
    public List<Observation> findByEncounterId(Long encounterId) {
        return observationRepository.findByEncounterId(encounterId);
    }

    public void delete(Long id) {
        observationRepository.deleteById(id);
    }
}