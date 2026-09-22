package com.medicalcontinuity.medicalcontinuity.service;

import com.medicalcontinuity.medicalcontinuity.entity.PatientMatch;
import com.medicalcontinuity.medicalcontinuity.entity.User;
import com.medicalcontinuity.medicalcontinuity.repository.PatientMatchRepository;
import com.medicalcontinuity.medicalcontinuity.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class PatientMatchService {

    private final PatientMatchRepository patientMatchRepository;
    private final UserRepository userRepository;

    public PatientMatch create(PatientMatch match) {
        if (match.getReviewStatus() == null) {
            match.setReviewStatus(PatientMatch.ReviewStatus.PENDING);
        }
        return patientMatchRepository.save(match);
    }

    @Transactional(readOnly = true)
    public List<PatientMatch> findByMatchRunId(Long matchRunId) {
        return patientMatchRepository.findByMatchRunIdOrderBySimilarityScoreDesc(matchRunId);
    }

    public PatientMatch review(Long id, PatientMatch.ReviewStatus decision, Long reviewedByUserId) {
        PatientMatch existing = patientMatchRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Patient match not found: " + id));
        existing.setReviewStatus(decision);
        existing.setReviewedByUser(findUser(reviewedByUserId));
        existing.setReviewedAt(LocalDateTime.now());
        return patientMatchRepository.save(existing);
    }

    private User findUser(Long userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("User not found: " + userId));
    }
}