package com.medicalcontinuity.medicalcontinuity.service;

import com.medicalcontinuity.medicalcontinuity.entity.Hospital;
import com.medicalcontinuity.medicalcontinuity.repository.HospitalRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional
public class HospitalService {

    private final HospitalRepository hospitalRepository;

    public Hospital create(Hospital hospital) {
        return hospitalRepository.save(hospital);
    }

    @Transactional(readOnly = true)
    public Optional<Hospital> findById(Long id) {
        return hospitalRepository.findById(id);
    }

    @Transactional(readOnly = true)
    public Optional<Hospital> findByRegistrationNumber(String registrationNumber) {
        return hospitalRepository.findByRegistrationNumber(registrationNumber);
    }

    @Transactional(readOnly = true)
    public List<Hospital> findAll() {
        return hospitalRepository.findAll();
    }

    public Hospital update(Long id, Hospital updated) {
        Hospital existing = hospitalRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Hospital not found: " + id));
        existing.setName(updated.getName());
        existing.setAddress(updated.getAddress());
        existing.setCity(updated.getCity());
        existing.setState(updated.getState());
        return hospitalRepository.save(existing);
    }

    public void delete(Long id) {
        hospitalRepository.deleteById(id);
    }
}