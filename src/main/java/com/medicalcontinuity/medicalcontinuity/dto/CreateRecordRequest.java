package com.medicalcontinuity.medicalcontinuity.dto;

import java.time.LocalDate;

public class CreateRecordRequest {
    private Long visitId;
    private String type;
    private String title;
    private String details;
    private String status;
    private LocalDate startDate;
    private LocalDate endDate;
    private String notes;

    public Long getVisitId() { return visitId; }
    public void setVisitId(Long visitId) { this.visitId = visitId; }
    public String getType() { return type; }
    public void setType(String type) { this.type = type; }
    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }
    public String getDetails() { return details; }
    public void setDetails(String details) { this.details = details; }
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
    public LocalDate getStartDate() { return startDate; }
    public void setStartDate(LocalDate startDate) { this.startDate = startDate; }
    public LocalDate getEndDate() { return endDate; }
    public void setEndDate(LocalDate endDate) { this.endDate = endDate; }
    public String getNotes() { return notes; }
    public void setNotes(String notes) { this.notes = notes; }
}
