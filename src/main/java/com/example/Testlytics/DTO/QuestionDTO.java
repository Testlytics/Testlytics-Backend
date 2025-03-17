package com.example.Testlytics.DTO;

import lombok.*;

import java.util.List;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class QuestionDTO {
    private UUID questionId;
    private UUID testId;
    private String questionText;
    private String answer;
    private List<OptionDTO> options;
}