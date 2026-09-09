package com.medicalcontinuity.medicalcontinuity.service;

import com.medicalcontinuity.medicalcontinuity.entity.Patient;
import com.medicalcontinuity.medicalcontinuity.entity.PatientMatch;
import com.medicalcontinuity.medicalcontinuity.entity.UnknownPatient;
import com.medicalcontinuity.medicalcontinuity.enums.PatientMatchStatus;
import com.medicalcontinuity.medicalcontinuity.exception.ResourceNotFoundException;
import com.medicalcontinuity.medicalcontinuity.repositories.PatientMatchRepository;
import com.medicalcontinuity.medicalcontinuity.repositories.PatientRepository;
import com.medicalcontinuity.medicalcontinuity.repositories.UnknownPatientRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.Period;
import java.util.*;

@Service
@Transactional
public class AiMatchingService {

    private final PatientRepository patientRepository;
    private final UnknownPatientRepository unknownPatientRepository;
    private final PatientMatchRepository patientMatchRepository;
    private final RestTemplate restTemplate;

    @Value("${ai.service.url:http://localhost:5000}")
    private String aiServiceUrl;

    public AiMatchingService(PatientRepository patientRepository,
                             UnknownPatientRepository unknownPatientRepository,
                             PatientMatchRepository patientMatchRepository,
                             RestTemplate restTemplate) {
        this.patientRepository = patientRepository;
        this.unknownPatientRepository = unknownPatientRepository;
        this.patientMatchRepository = patientMatchRepository;
        this.restTemplate = restTemplate;
    }

    @Transactional(readOnly = true)
    public Map<String, Object> getMatchesFromAI(Long unknownPatientId) {
        UnknownPatient unknownPatient = unknownPatientRepository.findById(unknownPatientId)
                .orElseThrow(() -> new ResourceNotFoundException("UnknownPatient not found with id: " + unknownPatientId));

        List<Patient> allPatients = patientRepository.findAll();

        Map<String, Object> unknownMap = convertUnknownPatient(unknownPatient);
        List<Map<String, Object>> candidateList = allPatients.stream()
                .map(this::convertPatient)
                .toList();

        Map<String, Object> requestBody = new HashMap<>();
        requestBody.put("unknown_patient", unknownMap);
        requestBody.put("candidates", candidateList);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<Map<String, Object>> entity = new HttpEntity<>(requestBody, headers);

        ResponseEntity<Map> response = restTemplate.exchange(
                aiServiceUrl + "/match",
                HttpMethod.POST,
                entity,
                Map.class
        );

        return response.getBody();
    }

    @Transactional
    public List<PatientMatch> runMatchAndSave(Long unknownPatientId) {
        Map<String, Object> aiResult = getMatchesFromAI(unknownPatientId);

        UnknownPatient unknownPatient = unknownPatientRepository.findById(unknownPatientId)
                .orElseThrow(() -> new ResourceNotFoundException("UnknownPatient not found with id: " + unknownPatientId));

        List<PatientMatch> savedMatches = new ArrayList<>();

        Object matchesObj = aiResult.get("matches");
        if (matchesObj instanceof List<?> matchesList) {
            for (Object matchObj : matchesList) {
                if (matchObj instanceof Map<?, ?> matchMap) {
                    Object candidateObj = matchMap.get("candidate");
                    Object scoreObj = matchMap.get("score");
                    Object confidenceObj = matchMap.get("confidence");

                    if (candidateObj instanceof Map<?, ?> candidateMap && scoreObj instanceof Number scoreNum) {
                        Long candidateId = ((Number) candidateMap.get("id")).longValue();
                        Patient candidatePatient = patientRepository.findById(candidateId)
                                .orElse(null);

                        if (candidatePatient != null) {
                            PatientMatch patientMatch = new PatientMatch();
                            patientMatch.setSimilarityScore(BigDecimal.valueOf(scoreNum.doubleValue()));
                            patientMatch.setMatchReason("AI Match - Confidence: " + confidenceObj);
                            patientMatch.setStatus(PatientMatchStatus.PENDING);
                            patientMatch.setUnknownPatient(unknownPatient);
                            patientMatch.setCandidatePatient(candidatePatient);

                            savedMatches.add(patientMatchRepository.save(patientMatch));
                        }
                    }
                }
            }
        }

        return savedMatches;
    }

    private Map<String, Object> convertUnknownPatient(UnknownPatient up) {
        Map<String, Object> map = new HashMap<>();
        map.put("id", up.getId());
        map.put("name", up.getDescription() != null ? up.getDescription() : "");
        map.put("gender", up.getGender() != null ? up.getGender().name() : "");
        map.put("village", up.getLocation() != null ? up.getLocation() : "");
        map.put("district", up.getLocation() != null ? up.getLocation() : "");
        map.put("phone", null);
        map.put("age", up.getApproximateAge());
        return map;
    }

    private Map<String, Object> convertPatient(Patient p) {
        Map<String, Object> map = new HashMap<>();
        map.put("id", p.getId());
        map.put("name", p.getName());
        map.put("gender", p.getGender() != null ? p.getGender().name() : "");
        map.put("village", p.getVillage() != null ? p.getVillage() : "");
        map.put("district", p.getDistrict() != null ? p.getDistrict() : "");
        map.put("phone", p.getPhone());

        if (p.getDateOfBirth() != null) {
            int age = Period.between(p.getDateOfBirth(), LocalDate.now()).getYears();
            map.put("age", age);
        } else {
            map.put("age", null);
        }

        return map;
    }
}
