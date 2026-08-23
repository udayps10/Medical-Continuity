package com.medicalcontinuity.medicalcontinuity.repository;

import com.medicalcontinuity.medicalcontinuity.entity.AccessLog;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AccessLogRepository extends JpaRepository<AccessLog, Long> {
}
