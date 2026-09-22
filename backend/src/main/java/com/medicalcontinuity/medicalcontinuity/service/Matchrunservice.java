package com.medicalcontinuity.medicalcontinuity.service;

import com.medicalcontinuity.medicalcontinuity.entity.MatchRun;
import com.medicalcontinuity.medicalcontinuity.repository.MatchRunRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional
public class MatchRunService {

    private final MatchRunRepository matchRunRepository;

    public MatchRun create(MatchRun matchRun) {
        matchRun.setStatus(MatchRun.Status.QUEUED);
        return matchRunRepository.save(matchRun);
    }

    @Transactional(readOnly = true)
    public Optional<MatchRun> findById(Long id) {
        return matchRunRepository.findById(id);
    }

    @Transactional(readOnly = true)
    public List<MatchRun> findByUnknownCaseId(Long unknownCaseId) {
        return matchRunRepository.findByUnknownCaseId(unknownCaseId);
    }

    public MatchRun updateStatus(Long id, MatchRun.Status status) {
        MatchRun existing = matchRunRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Match run not found: " + id));
        existing.setStatus(status);
        return matchRunRepository.save(existing);
    }
}