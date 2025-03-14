package com.example.Testlytics.DTO;

import lombok.*;

import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class OptionDTO {
    private UUID optionId;
    private String optionText;
    private boolean isCorrect;
}
