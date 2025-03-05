package com.example.Testlytics.DTO;

import java.time.LocalDateTime;

public class TestAttemptDTO {

    private Long attemptId;
    private Long testId;
    private String testDetails;  // New field
    private Long userId;
    private Double score;
    private LocalDateTime attemptStartTime;
    private LocalDateTime attemptEndTime;
    private String feedback;
    private String query;

    public TestAttemptDTO() {}

    public TestAttemptDTO(Long attemptId, Long testId, String testDetails, Long userId, Double score,
                          LocalDateTime attemptStartTime, LocalDateTime attemptEndTime, String feedback, String query) {
        this.attemptId = attemptId;
        this.testId = testId;
        this.testDetails = testDetails;
        this.userId = userId;
        this.score = score;
        this.attemptStartTime = attemptStartTime;
        this.attemptEndTime = attemptEndTime;
        this.feedback = feedback;
        this.query = query;
    }

    // Getters and Setters
    public Long getAttemptId() {
        return attemptId;
    }

    public void setAttemptId(Long attemptId) {
        this.attemptId = attemptId;
    }

    public Long getTestId() {
        return testId;
    }

    public void setTestId(Long testId) {
        this.testId = testId;
    }

    public String getTestDetails() {
        return testDetails;
    }

    public void setTestDetails(String testDetails) {
        this.testDetails = testDetails;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public Double getScore() {
        return score;
    }

    public void setScore(Double score) {
        this.score = score;
    }

    public LocalDateTime getAttemptStartTime() {
        return attemptStartTime;
    }

    public void setAttemptStartTime(LocalDateTime attemptStartTime) {
        this.attemptStartTime = attemptStartTime;
    }

    public LocalDateTime getAttemptEndTime() {
        return attemptEndTime;
    }

    public void setAttemptEndTime(LocalDateTime attemptEndTime) {
        this.attemptEndTime = attemptEndTime;
    }

    public String getFeedback() {
        return feedback;
    }

    public void setFeedback(String feedback) {
        this.feedback = feedback;
    }

    public String getQuery() {
        return query;
    }

    public void setQuery(String query) {
        this.query = query;
    }
}
