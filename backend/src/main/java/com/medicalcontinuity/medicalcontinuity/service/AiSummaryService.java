package com.medicalcontinuity.medicalcontinuity.service;

import com.medicalcontinuity.medicalcontinuity.entity.AiSummary;
import com.medicalcontinuity.medicalcontinuity.repository.AiSummaryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional
public class AiSummaryService {

    private final AiSummaryRepository aiSummaryRepository;

    public AiSummary create(AiSummary summary) {
        if (summary.getStatus() == null) {
            summary.setStatus(AiSummary.Status.DRAFT);
        }
        return aiSummaryRepository.save(summary);
    }

    @Transactional(readOnly = true)
    public Optional<AiSummary> findById(Long id) {
        return aiSummaryRepository.findById(id);
    }

    @Transactional(readOnly = true)
    public List<AiSummary> findByPatientId(Long patientId) {
        return aiSummaryRepository.findByPatientId(patientId);
    }

    @Transactional(readOnly = true)
    public List<AiSummary> findPublishedByPatientId(Long patientId) {
        return aiSummaryRepository.findByPatientIdAndStatus(patientId, AiSummary.Status.PUBLISHED);
    }

    public AiSummary publish(Long id) {
        AiSummary existing = aiSummaryRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("AI summary not found: " + id));
        existing.setStatus(AiSummary.Status.PUBLISHED);
        return aiSummaryRepository.save(existing);
    }
}