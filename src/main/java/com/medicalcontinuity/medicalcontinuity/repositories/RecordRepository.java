package com.medicalcontinuity.medicalcontinuity.repositories;

import com.medicalcontinuity.medicalcontinuity.entity.Record;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RecordRepository extends JpaRepository<Record, Long> {
}
