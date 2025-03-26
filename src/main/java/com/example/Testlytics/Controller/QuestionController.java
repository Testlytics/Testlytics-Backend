package com.example.Testlytics.Controller;

import com.example.Testlytics.DTO.QuestionDTO;
import com.example.Testlytics.Service.QuestionService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.UUID;

@RestController
@RequestMapping("/api/questions")
@RequiredArgsConstructor
public class QuestionController {

    @Autowired
    private final QuestionService questionService;

    // ✅ Create a question for a specific test
    @PostMapping("/{testId}")
    public ResponseEntity<QuestionDTO> createQuestion(
            @PathVariable UUID testId,
            @RequestBody Map<String, String> requestBody) {
        String questionText = requestBody.get("questionText");
        String answer = requestBody.get("answer");
        QuestionDTO createdQuestion = questionService.createQuestion(testId, questionText, answer);
        return ResponseEntity.ok(createdQuestion);
    }

    // ✅ Get all questions
    @GetMapping
    public ResponseEntity<List<QuestionDTO>> getAllQuestions() {
        List<QuestionDTO> questions = questionService.getAllQuestions();
        return ResponseEntity.ok(questions);
    }

    // ✅ Get a specific question by ID
    @GetMapping("/{questionId}")
    public ResponseEntity<QuestionDTO> getQuestionById(@PathVariable UUID questionId) {
        QuestionDTO question = questionService.getQuestionById(questionId);
        return ResponseEntity.ok(question);
    }

    // ✅ Get all questions for a given test ID
    @GetMapping("/test/{testId}")
    public ResponseEntity<List<QuestionDTO>> getQuestionsByTestId(@PathVariable UUID testId) {
        List<QuestionDTO> questions = questionService.getQuestionsByTestId(testId);
        return ResponseEntity.ok(questions);
    }
}
