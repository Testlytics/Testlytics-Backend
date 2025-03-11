package com.example.Testlytics.Entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "test_details")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder // ✅ This enables the builder pattern
public class TestDetails {

    @Id
    @GeneratedValue
    @Column(name = "test_id", updatable = false, nullable = false)
    private UUID testId;

    @Column(name = "subject_id", nullable = false)
    private UUID subjectId; // Foreign Key (Assume there's a Subject entity)

    @Column(name = "test_name", nullable = false)
    private String testName;

    @Column(name = "test_date", nullable = false)
    private LocalDate testDate;

    @Column(name = "test_duration", nullable = false)
    private int testDuration; // Duration in minutes

    @Column(name = "start_time", nullable = false)
    @JsonFormat(pattern = "HH:mm:ss")
    private LocalTime startTime;

    @Column(name = "end_time", nullable = false)
    @JsonFormat(pattern = "HH:mm:ss")
    private LocalTime endTime;

    @CreationTimestamp
    @Column(name = "created_on", updatable = false)
    private LocalDateTime createdOn;

    @UpdateTimestamp
    @Column(name = "modified_on")
    private LocalDateTime modifiedOn;

    @Column(name = "deleted_on")
    private LocalDateTime deletedOn;

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



    public void setCreatedOn(LocalDateTime createdOn) {
        this.createdOn = createdOn;
    }


    public void setModifiedOn(LocalDateTime modifiedOn) {
        this.modifiedOn = modifiedOn;
    }



    public void setDeletedOn(LocalDateTime deletedOn) {
        this.deletedOn = deletedOn;
    }

    // ✅ If you don't want to expose timestamps via DTO, consider marking them private
    private LocalDateTime getCreatedOn() {
        return createdOn;
    }

    private LocalDateTime getModifiedOn() {
        return modifiedOn;
    }

    private LocalDateTime getDeletedOn() {
        return deletedOn;
    }

    public static TestDetailsBuilder builder() {
        return new TestDetailsBuilder();
    }

    public static class TestDetailsBuilder {
        private final TestDetails testDetails;

        public TestDetailsBuilder() {
            this.testDetails = new TestDetails();
        }

        public TestDetailsBuilder testId(UUID testId) {
            testDetails.setTestId(testId);
            return this;
        }

        public TestDetailsBuilder subjectId(UUID subjectId) {
            testDetails.setSubjectId(subjectId);
            return this;
        }

        public TestDetailsBuilder testName(String testName) {
            testDetails.setTestName(testName);
            return this;
        }

        public TestDetailsBuilder testDate(LocalDate testDate) {
            testDetails.setTestDate(testDate);
            return this;
        }

        public TestDetailsBuilder testDuration(int testDuration) {
            testDetails.setTestDuration(testDuration);
            return this;
        }

        public TestDetailsBuilder startTime(LocalTime startTime) {
            testDetails.setStartTime(startTime);
            return this;
        }

        public TestDetailsBuilder endTime(LocalTime endTime) {
            testDetails.setEndTime(endTime);
            return this;
        }

        public TestDetails build() {
            return testDetails;
        }
    }

    // ✅ Method to check if a test is deleted (Soft Delete)
    public boolean isDeleted() {
        return deletedOn != null;
    }


}
