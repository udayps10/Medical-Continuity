package com.medicalcontinuity.medicalcontinuity.service;

import com.medicalcontinuity.medicalcontinuity.dto.UpdateRecordRequest;
import com.medicalcontinuity.medicalcontinuity.entity.*;
import com.medicalcontinuity.medicalcontinuity.repositories.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class DoctorService {

    @Autowired
    private DoctorRepository doctorRepository;

    @Autowired
    private PatientRepository patientRepository;

    @Autowired
    private VisitRepository visitRepository;

    @Autowired
    private RecordRepository recordRepository;

    @Autowired
    private AccessLogRepository accessLogRepository;

    public Optional<Doctor> getDoctorByUserId(Long userId) {
        return doctorRepository.findByUserId(userId);
    }

    public Map<String, Object> getPatientSummary(Long patientId, Long doctorId) {
        Doctor doctor = doctorRepository.findById(doctorId).orElseThrow(() -> new RuntimeException("Doctor not found"));
        Patient patient = patientRepository.findById(patientId).orElseThrow(() -> new RuntimeException("Patient not found"));

        if (!patient.isConsentGiven()) {
            Map<String, Object> err = new HashMap<>();
            err.put("error", "Patient has not given consent");
            return err;
        }

        AccessLog accessLog = new AccessLog();
        accessLog.setConsent(null);
        accessLog.setAccessedBy(doctor.getUser());
        accessLogRepository.save(accessLog);

        List<Visit> allVisits = visitRepository.findByPatientId(patientId);
        List<Record> allRecords = allVisits.stream()
                .flatMap(v -> recordRepository.findByVisitId(v.getId()).stream())
                .collect(Collectors.toList());

        Map<String, Object> summary = new HashMap<>();
        summary.put("allergy_alert", patient.getAllergies());
        summary.put("total_visits", allVisits.size());
        summary.put("total_records", allRecords.size());
        summary.put("visits", allVisits);
        summary.put("records", allRecords);
        return summary;
    }

    public Visit createVisit(CreateVisitRequest req) {
        Patient patient = patientRepository.findById(req.getPatientId()).orElseThrow(() -> new RuntimeException("Patient not found"));
        Hospital hospital = new Hospital();
        hospital.setId(req.getHospitalId());
        Doctor doctor = doctorRepository.findById(req.getDoctorId()).orElseThrow(() -> new RuntimeException("Doctor not found"));
        Visit visit = new Visit();
        visit.setPatient(patient);
        visit.setHospital(hospital);
        visit.setDoctor(doctor);
        visit.setDate(req.getDate());
        visit.setChiefComplaint(req.getChiefComplaint());
        return visitRepository.save(visit);
    }

    public Record addRecord(CreateRecordRequest req) {
        Visit visit = visitRepository.findById(req.getVisitId()).orElseThrow(() -> new RuntimeException("Visit not found"));
        Record record = new Record();
        record.setVisit(visit);
        record.setType(Record.Type.valueOf(req.getType().toUpperCase()));
        record.setTitle(req.getTitle());
        record.setDetails(req.getDetails());
        record.setStatus(Record.Status.valueOf(req.getStatus().toUpperCase()));
        record.setStartDate(req.getStartDate());
        record.setEndDate(req.getEndDate());
        record.setNotes(req.getNotes());
        return recordRepository.save(record);
    }

    public Record updateRecord(Long id, UpdateRecordRequest req) {
        Record record = recordRepository.findById(id).orElseThrow(() -> new RuntimeException("Record not found"));
        if (req.getStatus() != null) {
            record.setStatus(Record.Status.valueOf(req.getStatus().toUpperCase()));
        }
        if (req.getNotes() != null) {
            record.setNotes(req.getNotes());
        }
        return recordRepository.save(record);
    }
}
