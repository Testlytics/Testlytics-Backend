package com.example.Testlytics.DTO;

import lombok.*;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.UUID;

@Getter
@Setter
@Builder
public class TestDTO {
    private UUID testId;
    private UUID subjectId;
    private String testName;
    private LocalDate testDate;
    private int testDuration;
    private LocalTime startTime;
    private LocalTime endTime;
}
