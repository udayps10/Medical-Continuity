package com.medicalcontinuity.medicalcontinuity.service;

import com.medicalcontinuity.medicalcontinuity.entity.Hospital;
import com.medicalcontinuity.medicalcontinuity.entity.Patient;
import com.medicalcontinuity.medicalcontinuity.entity.PatientEncounter;
import com.medicalcontinuity.medicalcontinuity.repositories.HospitalRepository;
import com.medicalcontinuity.medicalcontinuity.repositories.PatientEncounterRepository;
import com.medicalcontinuity.medicalcontinuity.repositories.PatientRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class PatientEncounterService {

    private final PatientEncounterRepository patientEncounterRepository;
    private final PatientRepository patientRepository;
    private final HospitalRepository hospitalRepository;

    public PatientEncounterService(PatientEncounterRepository patientEncounterRepository,
                                   PatientRepository patientRepository,
                                   HospitalRepository hospitalRepository) {
        this.patientEncounterRepository = patientEncounterRepository;
        this.patientRepository = patientRepository;
        this.hospitalRepository = hospitalRepository;
    }

    public PatientEncounter create(PatientEncounter encounter) {
        Long patientId = encounter.getPatient().getId();
        Long hospitalId = encounter.getHospital().getId();
        Patient patient = patientRepository.findById(patientId)
                .orElseThrow(() -> new RuntimeException("Patient not found with id: " + patientId));
        Hospital hospital = hospitalRepository.findById(hospitalId)
                .orElseThrow(() -> new RuntimeException("Hospital not found with id: " + hospitalId));
        encounter.setPatient(patient);
        encounter.setHospital(hospital);
        return patientEncounterRepository.save(encounter);
    }

    @Transactional(readOnly = true)
    public PatientEncounter getById(Long id) {
        return patientEncounterRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("PatientEncounter not found with id: " + id));
    }

    @Transactional(readOnly = true)
    public List<PatientEncounter> getAll() {
        return patientEncounterRepository.findAll();
    }

    @Transactional(readOnly = true)
    public List<PatientEncounter> getByPatientId(Long patientId) {
        return patientEncounterRepository.findByPatientId(patientId);
    }

    @Transactional(readOnly = true)
    public List<PatientEncounter> getByHospitalId(Long hospitalId) {
        return patientEncounterRepository.findByHospitalId(hospitalId);
    }

    public PatientEncounter update(Long id, PatientEncounter updated) {
        PatientEncounter existing = getById(id);
        existing.setEncounterType(updated.getEncounterType());
        existing.setArrivalTime(updated.getArrivalTime());
        existing.setDischargeTime(updated.getDischargeTime());
        existing.setDoctorName(updated.getDoctorName());
        existing.setNotes(updated.getNotes());
        return patientEncounterRepository.save(existing);
    }

    public void delete(Long id) {
        PatientEncounter encounter = getById(id);
        patientEncounterRepository.delete(encounter);
    }
}
