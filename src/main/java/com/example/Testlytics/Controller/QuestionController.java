package com.example.Testlytics.Controller;

import com.example.Testlytics.DTO.QuestionDTO;
import com.example.Testlytics.Service.QuestionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/questions")
public class QuestionController {

    @Autowired
    private QuestionService questionService;

    // Create a new question (Multipart Form Data)
    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public QuestionDTO createQuestion(
            @RequestParam("test_id") UUID testId,
            @RequestParam("question") String questionText,
            @RequestParam(value = "image", required = false) MultipartFile image,
            @RequestParam("correct_option_id") UUID correctOptionId) {
        return questionService.createQuestion(testId, questionText, image, correctOptionId);
    }

    // Get all questions for a specific test
    @GetMapping("/{testId}")
    @PreAuthorize("hasAnyRole('STUDENT', 'ADMIN')")
    public List<QuestionDTO> getQuestionsByTestId(@PathVariable UUID testId) {
        return questionService.getQuestionsByTestId(testId);
    }

    // Get a specific question by ID
    @GetMapping("/{testId}/{questionId}")
    @PreAuthorize("hasAnyRole('STUDENT', 'ADMIN')")
    public QuestionDTO getQuestionById(@PathVariable UUID testId, @PathVariable UUID questionId) {
        return questionService.getQuestionById(testId, questionId);
    }

    // Update a question (Multipart Form Data)
    @PutMapping("/{testId}/{questionId}")
    @PreAuthorize("hasRole('ADMIN')")
    public QuestionDTO updateQuestion(
            @PathVariable UUID testId,
            @PathVariable UUID questionId,
            @RequestParam("question") String updatedQuestionText,
            @RequestParam(value = "image", required = false) MultipartFile updatedImage,
            @RequestParam("correct_option_id") UUID updatedCorrectOptionId) {
        return questionService.updateQuestion(testId, questionId, updatedQuestionText, updatedImage, updatedCorrectOptionId);
    }

    // Delete a question completely (No soft delete)
    @DeleteMapping("/{testId}/{questionId}")
    @PreAuthorize("hasRole('ADMIN')")
    public void deleteQuestion(@PathVariable UUID testId, @PathVariable UUID questionId) {
        questionService.deleteQuestion(testId, questionId);
    }
}
