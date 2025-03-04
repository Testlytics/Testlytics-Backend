package com.example.Testlytics.Entity;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;

import java.io.Serializable;
import java.util.Objects;

@Embeddable
public class TestAttemptId implements Serializable {

    @Column(name = "test_id")
    private Long testId;

    @Column(name = "user_id")
    private Long userId;

    public TestAttemptId() {}

    public TestAttemptId(Long testId, Long userId) {
        this.testId = testId;
        this.userId = userId;
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        TestAttemptId that = (TestAttemptId) o;
        return Objects.equals(testId, that.testId) && Objects.equals(userId, that.userId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(testId, userId);
    }
}
