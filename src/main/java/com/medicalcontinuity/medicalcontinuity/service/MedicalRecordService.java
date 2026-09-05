package com.medicalcontinuity.medicalcontinuity.service;

import com.medicalcontinuity.medicalcontinuity.entity.Hospital;
import com.medicalcontinuity.medicalcontinuity.entity.MedicalRecord;
import com.medicalcontinuity.medicalcontinuity.entity.Patient;
import com.medicalcontinuity.medicalcontinuity.exception.ResourceNotFoundException;
import com.medicalcontinuity.medicalcontinuity.repositories.HospitalRepository;
import com.medicalcontinuity.medicalcontinuity.repositories.MedicalRecordRepository;
import com.medicalcontinuity.medicalcontinuity.repositories.PatientRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class MedicalRecordService {

    private final MedicalRecordRepository medicalRecordRepository;
    private final PatientRepository patientRepository;
    private final HospitalRepository hospitalRepository;

    public MedicalRecordService(MedicalRecordRepository medicalRecordRepository,
                                PatientRepository patientRepository,
                                HospitalRepository hospitalRepository) {
        this.medicalRecordRepository = medicalRecordRepository;
        this.patientRepository = patientRepository;
        this.hospitalRepository = hospitalRepository;
    }

    public MedicalRecord create(MedicalRecord record) {
        Long patientId = record.getPatient().getId();
        Long hospitalId = record.getHospital().getId();
        Patient patient = patientRepository.findById(patientId)
                .orElseThrow(() -> new ResourceNotFoundException("Patient not found with id: " + patientId));
        Hospital hospital = hospitalRepository.findById(hospitalId)
                .orElseThrow(() -> new ResourceNotFoundException("Hospital not found with id: " + hospitalId));
        record.setPatient(patient);
        record.setHospital(hospital);
        return medicalRecordRepository.save(record);
    }

    @Transactional(readOnly = true)
    public MedicalRecord getById(Long id) {
        return medicalRecordRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("MedicalRecord not found with id: " + id));
    }

    @Transactional(readOnly = true)
    public List<MedicalRecord> getAll() {
        return medicalRecordRepository.findAll();
    }

    @Transactional(readOnly = true)
    public List<MedicalRecord> getByPatientId(Long patientId) {
        return medicalRecordRepository.findByPatientId(patientId);
    }

    @Transactional(readOnly = true)
    public List<MedicalRecord> getByHospitalId(Long hospitalId) {
        return medicalRecordRepository.findByHospitalId(hospitalId);
    }

    public MedicalRecord update(Long id, MedicalRecord updated) {
        MedicalRecord existing = getById(id);
        existing.setRecordType(updated.getRecordType());
        existing.setRecordDate(updated.getRecordDate());
        existing.setDoctorName(updated.getDoctorName());
        existing.setSummary(updated.getSummary());
        existing.setNotes(updated.getNotes());
        return medicalRecordRepository.save(existing);
    }

    public void delete(Long id) {
        MedicalRecord record = getById(id);
        medicalRecordRepository.delete(record);
    }
}
