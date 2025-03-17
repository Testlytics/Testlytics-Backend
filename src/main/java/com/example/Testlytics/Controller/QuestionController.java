package com.example.Testlytics.Controller;

import com.example.Testlytics.DTO.ApiResponse;
import com.example.Testlytics.DTO.QuestionDTO;
import com.example.Testlytics.Service.QuestionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/questions")
public class QuestionController {

    @Autowired
    private QuestionService questionService;

    @PostMapping("/{testId}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<QuestionDTO>> createQuestion(
            @PathVariable UUID testId, @RequestBody QuestionDTO questionDTO) {
        
        QuestionDTO createdQuestion = questionService.createQuestion(testId, questionDTO.getQuestionText(),
                questionDTO.getAnswer(), questionDTO.getOptions());

        return ResponseEntity.status(201)
                .body(new ApiResponse<>(201, "Success", "Question created successfully.", createdQuestion));
    }

    @GetMapping("/{questionId}")
    @PreAuthorize("hasAnyRole('ADMIN','STUDENT')")
    public ResponseEntity<ApiResponse<QuestionDTO>> getQuestionById(@PathVariable UUID questionId) {
        QuestionDTO question = questionService.getQuestionById(questionId);
        return ResponseEntity.ok(new ApiResponse<>(200, "Success", "Question retrieved successfully.", question));
    }

    @DeleteMapping("/{questionId}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<String>> deleteQuestion(@PathVariable UUID questionId) {
        questionService.deleteQuestion(questionId);
        return ResponseEntity.ok(new ApiResponse<>(200, "Success", "Question deleted successfully.", null));
    }
    @PutMapping("/{questionId}")
@PreAuthorize("hasRole('ADMIN')")
public ResponseEntity<ApiResponse<QuestionDTO>> updateQuestion(
        @PathVariable UUID questionId, @RequestBody QuestionDTO questionDTO) {
    
    QuestionDTO updatedQuestion = questionService.updateQuestion(
            questionId, 
            questionDTO.getQuestionText(), 
            questionDTO.getAnswer(), 
            questionDTO.getOptions()
    );

    return ResponseEntity.ok(new ApiResponse<>(200, "Success", "Question updated successfully.", updatedQuestion));
}

}
