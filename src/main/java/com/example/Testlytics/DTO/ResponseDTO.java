package com.example.Testlytics.DTO;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ResponseDTO {
    private Long responseId;
    private Long testId;
    private Long userId;
    private Long questionId;
    private Long selectedOptionId;
    private Boolean isCorrect;
}
