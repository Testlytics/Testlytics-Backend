package com.example.Testlytics.DTO;

import lombok.*;

import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@Builder
public class QuestionDTO {
    private UUID questionId;
    private UUID testId; // Only store testId, not the entire TestDetails entity
    private String questionText;
    private byte[] image; // Store image as byte[] instead of Base64
    private String answer;

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

    public byte[] getImage() {
        return image;
    }

    public void setImage(byte[] image) {
        this.image = image;
    }

    public String getAnswer() {
        return answer;
    }

    public void setAnswer(String answer) {
        this.answer = answer;
    }

    public QuestionDTO(UUID questionId, UUID testId, String questionText, byte[] image, String answer) {
        this.questionId = questionId;
        this.testId = testId;
        this.questionText = questionText;
        this.image = image;
        this.answer = answer;
    }

}
