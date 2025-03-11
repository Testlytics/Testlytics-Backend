package com.example.Testlytics.Entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.UUID;

@Entity
@Table(name = "responses")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Response {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private UUID responseId; // Primary Key

    @ManyToOne
    @JoinColumn(name = "test_id", nullable = false)
    private TestDetails test; // Foreign Key

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user; // Foreign Key

    @ManyToOne
    @JoinColumn(name = "question_id", nullable = false)
    private Question question; // Foreign Key

    private UUID selectedOptionId;
    private Boolean isCorrect;
}
