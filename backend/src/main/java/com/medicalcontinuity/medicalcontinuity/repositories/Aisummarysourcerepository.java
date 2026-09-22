package com.medicalcontinuity.medicalcontinuity.repository;

import com.medicalcontinuity.medicalcontinuity.entity.AiSummarySource;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface AiSummarySourceRepository extends JpaRepository<AiSummarySource, AiSummarySource.PK> {
    List<AiSummarySource> findBySummaryId(Long summaryId);
}