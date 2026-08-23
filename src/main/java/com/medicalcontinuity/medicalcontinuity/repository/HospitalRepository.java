package com.medicalcontinuity.medicalcontinuity.repository;

import com.medicalcontinuity.medicalcontinuity.entity.Hospital;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface HospitalRepository extends JpaRepository<Hospital, Long> {
}
