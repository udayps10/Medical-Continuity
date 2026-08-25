package com.medicalcontinuity.medicalcontinuity.repositories;

import com.medicalcontinuity.medicalcontinuity.entity.AccessLog;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AccessLogRepository extends JpaRepository<AccessLog, Long> {
    List<AccessLog> findByConsentId(Long consentId);
}
