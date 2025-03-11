package com.example.Testlytics.DTO;

import lombok.*;

import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ResponseDTO {
    private UUID responseId;
    private UUID testId;
    private Integer userId;
    private UUID questionId;
    private UUID selectedOptionId;
    private Boolean isCorrect;
}
