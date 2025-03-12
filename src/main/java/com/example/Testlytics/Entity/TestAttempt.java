package com.example.Testlytics.Entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class TestAttempt {

    @EmbeddedId
    private TestAttemptId id;

    private int score;
    private boolean completed;
    private LocalDateTime attemptStartTime;
    private LocalDateTime attemptEndTime;
    private String query;
    private String feedback;
}
