package com.example.Testlytics.Controller;

import com.example.Testlytics.DTO.ApiResponse;
import com.example.Testlytics.DTO.QuestionDTO;
import com.example.Testlytics.Service.QuestionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Map;
import java.util.UUID;

@RestController
@RequestMapping("/api/questions")
public class QuestionController {

    @Autowired
    private QuestionService questionService;

    // ✅ Create a question for a specific test
    @PostMapping("/{testId}")
    public ResponseEntity<ApiResponse<QuestionDTO>> createQuestion(
            @PathVariable UUID testId,
            @RequestBody Map<String, String> requestBody) {
        String questionText = requestBody.get("questionText");
        String answer = requestBody.get("answer");
        QuestionDTO createdQuestion = questionService.createQuestion(testId, questionText, answer);

        ApiResponse<QuestionDTO> response = new ApiResponse<>(
                201, "Success", "Question created successfully.", createdQuestion);
        return ResponseEntity.status(201).body(response);
    }

    // ✅ Get all questions
    @GetMapping
    public ResponseEntity<ApiResponse<List<QuestionDTO>>> getAllQuestions() {
        List<QuestionDTO> questions = questionService.getAllQuestions();

        ApiResponse<List<QuestionDTO>> response = new ApiResponse<>(
                200, "Success", "All questions retrieved successfully.", questions);
        return ResponseEntity.ok(response);
    }

    // ✅ Get a specific question by ID
    @GetMapping("/{questionId}")
    public ResponseEntity<ApiResponse<QuestionDTO>> getQuestionById(@PathVariable UUID questionId) {
        QuestionDTO question = questionService.getQuestionById(questionId);

        ApiResponse<QuestionDTO> response = new ApiResponse<>(
                200, "Success", "Question retrieved successfully.", question);
        return ResponseEntity.ok(response);
    }

    // ✅ Get all questions for a given test ID
    @GetMapping("/test/{testId}")
    public ResponseEntity<ApiResponse<List<QuestionDTO>>> getQuestionsByTestId(@PathVariable UUID testId) {
        List<QuestionDTO> questions = questionService.getQuestionsByTestId(testId);

        ApiResponse<List<QuestionDTO>> response = new ApiResponse<>(
                200, "Success", "Questions retrieved successfully for the given test ID.", questions);
        return ResponseEntity.ok(response);
    }

    // ✅ Update a question for a specific test
    @PutMapping("/{testId}/{questionId}")
    public ResponseEntity<ApiResponse<QuestionDTO>> updateQuestion(
            @PathVariable UUID testId,
            @PathVariable UUID questionId,
            @RequestBody Map<String, String> requestBody) {
        String questionText = requestBody.get("questionText");
        String answer = requestBody.get("answer");
        QuestionDTO updatedQuestion = questionService.updateQuestion(testId, questionId, questionText, answer);

        ApiResponse<QuestionDTO> response = new ApiResponse<>(
                200, "Success", "Question updated successfully.", updatedQuestion);
        return ResponseEntity.ok(response);
    }



    // ✅ Delete a question for a specific test
    @DeleteMapping("/{testId}/{questionId}")
    public ResponseEntity<ApiResponse<String>> deleteQuestion(
            @PathVariable UUID testId,
            @PathVariable UUID questionId) {
        questionService.deleteQuestion(testId, questionId);

        ApiResponse<String> response = new ApiResponse<>(
                200, "Success", "Question deleted successfully.", null);
        return ResponseEntity.ok(response);
    }


    // ✅ Upload Image for Question
    @PostMapping("/{questionId}/image")
    public ResponseEntity<ApiResponse<String>> uploadQuestionImage(
            @PathVariable UUID questionId,
            @RequestParam("image") MultipartFile imageFile) {

        String responseMessage = questionService.uploadQuestionImage(questionId, imageFile);
        ApiResponse<String> response = new ApiResponse<>(
                200, "Success", "Image uploaded successfully", responseMessage);

        return ResponseEntity.ok(response);
    }

    // ✅ Retrieve Image for a Question
    @GetMapping("/{questionId}/image")
    public ResponseEntity<ApiResponse<byte[]>> getQuestionImage(@PathVariable UUID questionId) {

        byte[] imageData = questionService.getQuestionImage(questionId);
        ApiResponse<byte[]> response = new ApiResponse<>(
                200, "Success", "Image retrieved successfully", imageData);

        return ResponseEntity.ok()
                .contentType(MediaType.APPLICATION_JSON) // Keeping JSON format for structured API response
                .body(response);
    }

    @PutMapping("/{questionId}/image")
    public ResponseEntity<ApiResponse<String>> updateQuestionImage(
            @PathVariable UUID questionId,
            @RequestParam("image") MultipartFile imageFile) {

        String result = questionService.updateQuestionImage(questionId, imageFile);

        if (result.equals("Question not found, unable to update image.")) {
            ApiResponse<String> response = new ApiResponse<>(404, "Failure", result, null);
            return ResponseEntity.status(404).body(response);
        } else if (result.startsWith("Error updating image")) {
            ApiResponse<String> response = new ApiResponse<>(500, "Failure", result, null);
            return ResponseEntity.status(500).body(response);
        }

        ApiResponse<String> response = new ApiResponse<>(200, "Success", result, result);
        return ResponseEntity.ok(response);
    }


}
