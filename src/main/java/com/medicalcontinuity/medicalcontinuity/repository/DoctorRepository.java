package com.medicalcontinuity.medicalcontinuity.repository;

import com.medicalcontinuity.medicalcontinuity.entity.Doctor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DoctorRepository extends JpaRepository<Doctor, Long> {
}
