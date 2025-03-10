package com.example.Testlytics.Entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.UuidGenerator;

import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "questions")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Question {
    @Id
    @GeneratedValue
    @UuidGenerator
    @Column(name = "question_id", updatable = false, nullable = false)
    private UUID questionId;

    @ManyToOne
    @JoinColumn(name = "test_id", nullable = false) // Foreign key reference
    private TestDetails testDetails;

    @OneToMany(mappedBy = "question", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Options> options;

    @Column(name = "question_text", nullable = false)
    private String questionText;

    @Column(name = "answer", nullable = false)
    private String answer;

    @Lob
    @Column(name = "image")
    private byte[] image;

    // Constructor without image
    public Question(TestDetails testDetails, String questionText, String answer) {
        this.testDetails = testDetails;
        this.questionText = questionText;
        this.answer = answer;
    }

    public UUID getQuestionId() {
        return questionId;
    }

    public void setQuestionId(UUID questionId) {
        this.questionId = questionId;
    }

    public TestDetails getTestDetails() {
        return testDetails;
    }

    public void setTestDetails(TestDetails testDetails) {
        this.testDetails = testDetails;
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

    public byte[] getImage() {
        return image;
    }

    public void setImage(byte[] image) {
        this.image = image;
    }
}
