package com.medicalcontinuity.medicalcontinuity.repositories;

import com.medicalcontinuity.medicalcontinuity.entity.Visit;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface VisitRepository extends JpaRepository<Visit, Long> {
    List<Visit> findByPatientId(Long patientId);
    List<Visit> findByHospitalId(Long hospitalId);
}
