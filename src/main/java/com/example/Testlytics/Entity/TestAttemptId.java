package com.example.Testlytics.Entity;

import java.io.Serializable;

public class TestAttemptId implements Serializable {

    private Long testId;
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
}
