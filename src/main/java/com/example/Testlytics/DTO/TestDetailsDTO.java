package com.example.Testlytics.DTO;

import lombok.*;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.UUID;

@Getter
@Setter
@Builder
public class TestDetailsDTO {
    private UUID testId;
    private UUID subjectId;
    private String testName;
    private LocalDate testDate;
    private int testDuration;
    private LocalTime startTime;
    private LocalTime endTime;

    public UUID getTestId() {
        return testId;
    }

    public void setTestId(UUID testId) {
        this.testId = testId;
    }

    public UUID getSubjectId() {
        return subjectId;
    }

    public void setSubjectId(UUID subjectId) {
        this.subjectId = subjectId;
    }

    public String getTestName() {
        return testName;
    }

    public void setTestName(String testName) {
        this.testName = testName;
    }

    public LocalDate getTestDate() {
        return testDate;
    }

    public void setTestDate(LocalDate testDate) {
        this.testDate = testDate;
    }

    public int getTestDuration() {
        return testDuration;
    }

    public void setTestDuration(int testDuration) {
        this.testDuration = testDuration;
    }

    public LocalTime getStartTime() {
        return startTime;
    }

    public void setStartTime(LocalTime startTime) {
        this.startTime = startTime;
    }

    public LocalTime getEndTime() {
        return endTime;
    }

    public void setEndTime(LocalTime endTime) {
        this.endTime = endTime;
    }

    // Default constructor (Required)
    public TestDetailsDTO() {}

    // Constructor with all fields
    public TestDetailsDTO(UUID testId, UUID subjectId, String testName, LocalDate testDate,
                          int testDuration, LocalTime startTime, LocalTime endTime) {
        this.testId = testId;
        this.subjectId = subjectId;
        this.testName = testName;
        this.testDate = testDate;
        this.testDuration = testDuration;
        this.startTime = startTime;
        this.endTime = endTime;
    }

}
