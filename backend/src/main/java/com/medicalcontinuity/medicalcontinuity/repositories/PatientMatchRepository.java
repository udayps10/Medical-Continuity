package com.medicalcontinuity.medicalcontinuity.repositories;

import com.medicalcontinuity.medicalcontinuity.entity.PatientMatch;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PatientMatchRepository extends JpaRepository<PatientMatch, Long> {

    List<PatientMatch> findByUnknownPatientId(Long unknownPatientId);

    List<PatientMatch> findByCandidatePatientId(Long candidatePatientId);
}
