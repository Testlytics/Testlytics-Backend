package com.example.Testlytics.Entity;

import jakarta.persistence.Embeddable;
import lombok.*;

import java.io.Serializable;
import java.util.UUID;

@Embeddable
@Data
@NoArgsConstructor
@AllArgsConstructor
public class TestAttemptId implements Serializable {
    private UUID testId;
    private Integer userId;
}
