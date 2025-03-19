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
    @JoinColumn(name = "test_id", referencedColumnName = "test_id", nullable = false)
    private Test test;
 
    @Column(name = "question_text", nullable = false)
    private String questionText;
 
    @Column(name = "answer", nullable = false)
    private String answer;
    // New field to store image as byte[]
    @Lob
    private String image;
}