package com.drn.repository;

import com.drn.entity.MatchRun;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface MatchRunRepository extends JpaRepository<MatchRun, Long> {
    List<MatchRun> findByUnknownCaseId(Long unknownCaseId);
    List<MatchRun> findByStatus(MatchRun.Status status);
}