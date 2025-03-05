package com.example.Testlytics.Entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "test_attempts")
public class TestAttempt {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long attemptId;  // New Primary Key

    @Column(name = "test_id", nullable = false)
    private Long testId;  // Foreign Key reference to Test entity

    @Column(name = "user_id", nullable = false)
    private Long userId;  // Foreign Key reference to User entity

    @Column(name = "score")
    private Double score;

    @Column(name = "attempt_start_time", nullable = false)
    private LocalDateTime attemptStartTime;

    @Column(name = "attempt_end_time")
    private LocalDateTime attemptEndTime;

    @Column(name = "feedback")
    private String feedback;

    @Column(name = "query")
    private String query;

    public TestAttempt() {}

    public TestAttempt(Long testId, Long userId, LocalDateTime attemptStartTime) {
        this.testId = testId;
        this.userId = userId;
        this.attemptStartTime = attemptStartTime;
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
