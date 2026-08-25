package com.medicalcontinuity.medicalcontinuity.service;

import com.medicalcontinuity.medicalcontinuity.entity.*;
import com.medicalcontinuity.medicalcontinuity.repositories.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AccessLogService {

    @Autowired
    private AccessLogRepository accessLogRepository;

    public List<AccessLog> getByConsentId(Long consentId) {
        return accessLogRepository.findByConsentId(consentId);
    }

    public List<AccessLog> getAll() {
        return accessLogRepository.findAll();
    }
}
