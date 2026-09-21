package com.drn.repository;

import com.drn.entity.PatientMatch;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PatientMatchRepository extends JpaRepository<PatientMatch, Long> {
    List<PatientMatch> findByMatchRunIdOrderBySimilarityScoreDesc(Long matchRunId);
    List<PatientMatch> findByCandidatePatientId(Long candidatePatientId);
    List<PatientMatch> findByReviewStatus(PatientMatch.ReviewStatus reviewStatus);
}