package com.medicalcontinuity.medicalcontinuity.repositories;

import com.medicalcontinuity.medicalcontinuity.entity.Doctor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface DoctorRepository extends JpaRepository<Doctor, Long> {
    List<Doctor> findByHospitalId(Long hospitalId);
    Optional<Doctor> findByUserId(Long userId);
}
