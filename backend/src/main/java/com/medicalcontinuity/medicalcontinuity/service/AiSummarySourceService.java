package com.medicalcontinuity.medicalcontinuity.service;

import com.medicalcontinuity.medicalcontinuity.entity.AiSummarySource;
import com.medicalcontinuity.medicalcontinuity.repository.AiSummarySourceRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class AiSummarySourceService {

    private final AiSummarySourceRepository aiSummarySourceRepository;

    public AiSummarySource create(AiSummarySource source) {
        return aiSummarySourceRepository.save(source);
    }

    @Transactional(readOnly = true)
    public List<AiSummarySource> findBySummaryId(Long summaryId) {
        return aiSummarySourceRepository.findBySummaryId(summaryId);
    }
}