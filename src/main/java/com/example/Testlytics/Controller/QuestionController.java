package com.example.Testlytics.Controller;

import com.example.Testlytics.DTO.ApiResponse;
import com.example.Testlytics.DTO.QuestionDTO;
import com.example.Testlytics.Service.QuestionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
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

    // Create a new question (Without Image)
    @PostMapping("/{testId}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<QuestionDTO>> createQuestion(
            @PathVariable String testId,
            @RequestParam("question") String questionText,
            @RequestParam("answer") String answer) {
        QuestionDTO question = questionService.createQuestion(UUID.fromString(testId), questionText, null, answer);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(new ApiResponse<>(201, "Created", "Question created successfully", question));
    }

    // Upload an image for a specific question
    @PostMapping("/{questionId}/image")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<String>> uploadQuestionImage(
            @PathVariable UUID questionId,
            @RequestParam("image") MultipartFile image) {
        questionService.uploadImage(questionId, image);
        return ResponseEntity.ok(new ApiResponse<>(200, "OK", "Image uploaded successfully", null));
    }

    // Update an existing image for a question
    @PutMapping("/{questionId}/image")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<String>> updateQuestionImage(
            @PathVariable UUID questionId,
            @RequestParam("image") MultipartFile image) {
        questionService.updateImage(questionId, image);
        return ResponseEntity.ok(new ApiResponse<>(200, "OK", "Image updated successfully", null));
    }

    @GetMapping("/{questionId}/image")
    public ResponseEntity<ApiResponse<String>> getQuestionImage(@PathVariable UUID questionId) {
        String base64Image = questionService.getImageByQuestionId(questionId);
        return ResponseEntity.ok(new ApiResponse<>(200, "OK", "Image retrieved successfully", base64Image));
    }


    // Get all questions for a specific test
    @GetMapping("/{testId}")
    @PreAuthorize("hasAnyRole('STUDENT', 'ADMIN')")
    public ResponseEntity<ApiResponse<List<QuestionDTO>>> getQuestionsByTestId(@PathVariable UUID testId) {
        List<QuestionDTO> questions = questionService.getQuestionsByTestId(testId);
        return ResponseEntity.ok(new ApiResponse<>(200, "OK", "Questions retrieved successfully", questions));
    }

    // Get a specific question by ID
    @GetMapping("/{testId}/{questionId}")
    @PreAuthorize("hasAnyRole('STUDENT', 'ADMIN')")
    public ResponseEntity<ApiResponse<QuestionDTO>> getQuestionById(@PathVariable UUID testId, @PathVariable UUID questionId) {
        QuestionDTO question = questionService.getQuestionById(testId, questionId);
        return ResponseEntity.ok(new ApiResponse<>(200, "OK", "Question retrieved successfully", question));
    }

    // Update a question (Without Image)
    @PutMapping("/{testId}/{questionId}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<QuestionDTO>> updateQuestion(
            @PathVariable UUID testId,
            @PathVariable UUID questionId,
            @RequestParam("question") String updatedQuestionText,
            @RequestParam("answer") String updatedAnswer) {
        QuestionDTO updatedQuestion = questionService.updateQuestion(testId, questionId, updatedQuestionText, null, updatedAnswer);
        return ResponseEntity.ok(new ApiResponse<>(200, "OK", "Question updated successfully", updatedQuestion));
    }

    // Delete a question completely (No soft delete)
    @DeleteMapping("/{testId}/{questionId}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<String>> deleteQuestion(@PathVariable UUID testId, @PathVariable UUID questionId) {
        questionService.deleteQuestion(testId, questionId);
        return ResponseEntity.ok(new ApiResponse<>(200, "OK", "Question deleted successfully", null));
    }
}
