//package com.example.Testlytics.Entity;
//
//import jakarta.persistence.*;
//import lombok.*;
//
//import java.util.UUID;
//
//@Entity
//@Table(name = "options")
//@Getter
//@Setter
//@NoArgsConstructor
//@AllArgsConstructor
//@Builder
//public class Options {
//
//    @Id
//    @GeneratedValue
//    @Column(name = "option_id", updatable = false, nullable = false)
//    private UUID optionId;
//
//    @ManyToOne
//    @JoinColumn(name = "question_id", nullable = false)
//    private Question question;
//
//    @Column(name = "option_text", nullable = false)
//    private String optionText;
//
//    @Column(name = "is_correct", nullable = false)
//    private boolean isCorrect;
//
//    public UUID getOptionId() {
//        return optionId;
//    }
//
//    public void setOptionId(UUID optionId) {
//        this.optionId = optionId;
//    }
//
//    public Question getQuestion() {
//        return question;
//    }
//
//    public void setQuestion(Question question) {
//        this.question = question;
//    }
//
//    public String getOptionText() {
//        return optionText;
//    }
//
//    public void setOptionText(String optionText) {
//        this.optionText = optionText;
//    }
//
//    public boolean isCorrect() {  // Getter for boolean fields uses "is"
//        return isCorrect;
//    }
//
//    public void setCorrect(boolean correct) {  // Setter should be "setCorrect"
//        this.isCorrect = correct;
//    }
//}
//
