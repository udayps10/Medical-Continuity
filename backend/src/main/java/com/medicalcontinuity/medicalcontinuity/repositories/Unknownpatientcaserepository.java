package com.drn.repository;

import com.drn.entity.UnknownPatientCase;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface UnknownPatientCaseRepository extends JpaRepository<UnknownPatientCase, Long> {
    Optional<UnknownPatientCase> findByTemporaryId(String temporaryId);
    List<UnknownPatientCase> findByStatus(UnknownPatientCase.Status status);
    List<UnknownPatientCase> findByCapturedByHospitalId(Long hospitalId);
}