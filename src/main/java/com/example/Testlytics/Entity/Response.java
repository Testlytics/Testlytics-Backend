package com.example.Testlytics.Entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "responses")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Response {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "response_id")
    private Long responseId;

    @ManyToOne
    @JoinColumn(name = "test_id", nullable = false)
    private TestAttempt test; // Assuming a `Test` entity exists

    @ManyToOne
    @JoinColumn(name = "question_id", nullable = false)
    private Question question; // Assuming a `Question` entity exists

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user; // Assuming a `User` entity exists

    @Column(name = "selected_option_id", nullable = false)
    private Long selectedOptionId;

    @Column(name = "is_correct", nullable = false)
    private Boolean isCorrect;
}
