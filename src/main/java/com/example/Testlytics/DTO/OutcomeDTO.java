package com.example.Testlytics.DTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
public class OutcomeDTO {

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class SubmitOutcome {
        private UUID testId;
        private UUID questionId;
        private Integer userId;
        private UUID selectedOptionId;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class OutcomeDetails {
        private UUID outcomeId;
        private UUID testId;
        private UUID questionId;
        private Integer userId;
        private UUID selectedOptionId;
        private Boolean isCorrect; // Keep this for returning data
    }
}
