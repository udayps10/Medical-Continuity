package com.medicalcontinuity.medicalcontinuity.repositories;

import com.medicalcontinuity.medicalcontinuity.entity.UnknownPatient;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UnknownPatientRepository extends JpaRepository<UnknownPatient, Long> {

    Optional<UnknownPatient> findByTemporaryId(String temporaryId);

    boolean existsByTemporaryId(String temporaryId);
}
