package com.example.Testlytics.Entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.UUID;

@Entity
@Table(name = "questions")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Question {

    @Id
    @GeneratedValue
    @Column(name = "question_id", updatable = false, nullable = false)
    private UUID questionId;

    @ManyToOne
    @JoinColumn(name = "test_id", nullable = false)
    private TestDetails test; // Foreign key reference to TestDetails

    @Column(name = "question", nullable = false, columnDefinition = "TEXT")
    private String questionText;

    @Lob
    @Column(name = "image", columnDefinition = "BYTEA")
    private byte[] image;

    @Column(name = "answer", nullable = false, columnDefinition = "TEXT")
    private String answer; // Stores the correct answer as text

    public UUID getQuestionId() {
        return questionId;
    }

    public void setQuestionId(UUID questionId) {
        this.questionId = questionId;
    }

    public TestDetails getTest() {
        return test;
    }

    public void setTest(TestDetails test) {
        this.test = test;
    }

    public byte[] getImage() {
        return image;
    }

    public void setImage(byte[] image) {
        this.image = image;
    }

    public String getQuestionText() {
        return questionText;
    }

    public void setQuestionText(String questionText) {
        this.questionText = questionText;
    }

    public String getAnswer() {
        return answer;
    }

    public void setAnswer(String answer) {
        this.answer = answer;
    }
}
