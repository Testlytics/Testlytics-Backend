package com.example.Testlytics.DTO;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.UUID;

public class TestAttemptDTO {

    @Getter
    @Setter
    public static class TestAttemptStartRequest {
        private UUID testId;
        private Integer userId;
    }

    @Getter
    @Setter
    public static class TestAttemptSubmitRequest {
        private int score;
        private String query;
        private String feedback;
    }

    @Getter
    @Setter
    public static class UserAttendanceResponse {
        private UUID testId;
        private String testName;
        private String subject;
        private int score;
        private boolean completed;
        private LocalDateTime attemptStartTime;
        private LocalDateTime attemptEndTime;

        public UserAttendanceResponse(UUID testId, String testName, String subject, int score, boolean completed, LocalDateTime attemptStartTime, LocalDateTime attemptEndTime) {
            this.testId = testId;
            this.testName = testName;
            this.subject = subject;
            this.score = score;
            this.completed = completed;
            this.attemptStartTime = attemptStartTime;
            this.attemptEndTime = attemptEndTime;
        }
    }
}
