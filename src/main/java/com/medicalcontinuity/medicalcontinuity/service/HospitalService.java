package com.medicalcontinuity.medicalcontinuity.service;

import com.medicalcontinuity.medicalcontinuity.entity.*;
import com.medicalcontinuity.medicalcontinuity.repositories.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class HospitalService {

    @Autowired
    private HospitalRepository hospitalRepository;

    @Autowired
    private DoctorRepository doctorRepository;

    @Autowired
    private VisitRepository visitRepository;

    @Autowired
    private UserRepository userRepository;

    public Hospital createHospital(String name, String city) {
        Hospital hospital = new Hospital(name, city);
        return hospitalRepository.save(hospital);
    }

    public List<Hospital> getAllHospitals() {
        return hospitalRepository.findAll();
    }

    public Hospital getHospitalById(Long id) {
        return hospitalRepository.findById(id).orElseThrow(() -> new RuntimeException("Hospital not found"));
    }

    public Doctor addDoctor(Long userId, Long hospitalId, String specialization) {
        User user = userRepository.findById(userId).orElseThrow(() -> new RuntimeException("User not found"));
        Hospital hospital = hospitalRepository.findById(hospitalId).orElseThrow(() -> new RuntimeException("Hospital not found"));
        Doctor doctor = new Doctor();
        doctor.setUser(user);
        doctor.setHospital(hospital);
        doctor.setSpecialization(specialization);
        return doctorRepository.save(doctor);
    }

    public List<Doctor> getDoctors(Long hospitalId) {
        return doctorRepository.findByHospitalId(hospitalId);
    }

    public List<Visit> getVisits(Long hospitalId) {
        return visitRepository.findByHospitalId(hospitalId);
    }
}
