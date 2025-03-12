package com.example.Testlytics.Entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.UUID;

@Entity
@Data
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Response {

    @Id
    @GeneratedValue
    private UUID responseId; // Primary Key (UUID)

    @Column(nullable = false)
    private UUID testId; // Foreign Key (UUID)

    @Column(nullable = false)
    private UUID questionId; // Foreign Key (UUID)

    @Column(nullable = false)
    private Integer userId; // Foreign Key (Integer)

    private UUID selectedOptionId;
    private Boolean isCorrect;
}
