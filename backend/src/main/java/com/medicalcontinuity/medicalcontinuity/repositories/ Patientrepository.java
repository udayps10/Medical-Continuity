package com.drn.repository;

import com.drn.entity.Patient;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface PatientRepository extends JpaRepository<Patient, Long> {
    Optional<Patient> findByMcid(String mcid);
    Optional<Patient> findByUserId(Long userId);
}