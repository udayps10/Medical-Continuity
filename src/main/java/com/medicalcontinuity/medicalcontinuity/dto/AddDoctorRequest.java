package com.medicalcontinuity.medicalcontinuity.dto;

public class AddDoctorRequest {
    private Long userId;
    private Long hospitalId;
    private String specialization;

    public Long getUserId() { return userId; }
    public void setUserId(Long userId) { this.userId = userId; }
    public Long getHospitalId() { return hospitalId; }
    public void setHospitalId(Long hospitalId) { this.hospitalId = hospitalId; }
    public String getSpecialization() { return specialization; }
    public void setSpecialization(String specialization) { this.specialization = specialization; }
}
