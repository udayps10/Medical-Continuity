package com.drn.repository;

import com.drn.entity.AiSummary;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface AiSummaryRepository extends JpaRepository<AiSummary, Long> {
    List<AiSummary> findByPatientId(Long patientId);
    List<AiSummary> findByPatientIdAndStatus(Long patientId, AiSummary.Status status);
}
