package com.medicalcontinuity.medicalcontinuity.service;

import com.medicalcontinuity.medicalcontinuity.entity.Hospital;
import com.medicalcontinuity.medicalcontinuity.exception.ResourceNotFoundException;
import com.medicalcontinuity.medicalcontinuity.repositories.HospitalRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class HospitalService {

    private final HospitalRepository hospitalRepository;

    public HospitalService(HospitalRepository hospitalRepository) {
        this.hospitalRepository = hospitalRepository;
    }

    public Hospital createHospital(Hospital hospital) {
        return hospitalRepository.save(hospital);
    }

    @Transactional(readOnly = true)
    public Hospital getHospitalById(Long id) {
        return hospitalRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Hospital not found with id: " + id));
    }

    @Transactional(readOnly = true)
    public List<Hospital> getAllHospitals() {
        return hospitalRepository.findAll();
    }

    public Hospital updateHospital(Long id, Hospital updated) {
        Hospital existing = getHospitalById(id);
        existing.setName(updated.getName());
        existing.setRegistrationNumber(updated.getRegistrationNumber());
        existing.setAddress(updated.getAddress());
        existing.setCity(updated.getCity());
        existing.setState(updated.getState());
        existing.setPhone(updated.getPhone());
        return hospitalRepository.save(existing);
    }

    public void deleteHospital(Long id) {
        Hospital hospital = getHospitalById(id);
        hospitalRepository.delete(hospital);
    }
}
