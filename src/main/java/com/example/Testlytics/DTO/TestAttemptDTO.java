package com.example.Testlytics.DTO;

import lombok.Getter;
import lombok.Setter;

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
}
