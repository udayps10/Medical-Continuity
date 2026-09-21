package com.drn.repository;

import com.drn.entity.Observation;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ObservationRepository extends JpaRepository<Observation, Long> {
    List<Observation> findByEncounterId(Long encounterId);
}