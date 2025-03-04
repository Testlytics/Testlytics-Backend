package com.example.Testlytics.Entity;

import jakarta.persistence.*;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Objects;

@Entity
@Table(name = "test_attempts")
public class TestAttempt {

    @EmbeddedId
    private TestAttemptId id;

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

    public TestAttempt(TestAttemptId id, LocalDateTime attemptStartTime) {
        this.id = id;
        this.attemptStartTime = attemptStartTime;
    }

    public TestAttemptId getId() {
        return id;
    }

    public void setId(TestAttemptId id) {
        this.id = id;
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