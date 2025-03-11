package com.example.Testlytics.DTO;

import lombok.*;

import java.util.Base64;
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
    private String image;

    // Constructor without image
    public QuestionDTO(UUID questionId, UUID testId, String questionText, String answer) {
        this.questionId = questionId;
        this.testId = testId;
        this.questionText = questionText;
        this.answer = answer;
    }

    // Constructor with byte[] image (to Base64 conversion)
    public QuestionDTO(UUID questionId, UUID testId, String questionText, String answer, byte[] imageBytes) {
        this.questionId = questionId;
        this.testId = testId;
        this.questionText = questionText;
        this.answer = answer;
        this.image = (imageBytes != null) ? Base64.getEncoder().encodeToString(imageBytes) : null;
    }

    public UUID getQuestionId() {
        return questionId;
    }

    public void setQuestionId(UUID questionId) {
        this.questionId = questionId;
    }

    public UUID getTestId() {
        return testId;
    }

    public void setTestId(UUID testId) {
        this.testId = testId;
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

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }
}
