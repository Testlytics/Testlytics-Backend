package com.example.Testlytics.Controller;
 
import com.example.Testlytics.DTO.ApiResponse;
import com.example.Testlytics.DTO.QuestionDTO;
import com.example.Testlytics.Service.QuestionService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
 
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.UUID;
import java.util.logging.Logger;
 

@RestController
@RequestMapping("/api")
public class QuestionController {
 
    private static final Logger logger = Logger.getLogger(QuestionController.class.getName());
 
    @Autowired
    private QuestionService questionService;
 
    @Autowired
    private ObjectMapper objectMapper;
 
    @PostMapping(value = "/tests/{testId}/questions", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<QuestionDTO>> createQuestion(
            @PathVariable UUID testId,
            @RequestParam("question") String questionJson,
            @RequestParam(value = "image", required = false) MultipartFile image) {
 
        try {
            // ✅ Log the received JSON
            logger.info("Received JSON: " + questionJson);
 
            // ✅ Fix encoding issue (force UTF-8)
            questionJson = new String(questionJson.getBytes(StandardCharsets.ISO_8859_1), StandardCharsets.UTF_8);
 
            // ✅ Parse JSON into QuestionDTO
            QuestionDTO questionDTO = objectMapper.readValue(questionJson, QuestionDTO.class);
 
            // ✅ Call service to create the question
            QuestionDTO createdQuestion = questionService.createQuestion(
                    testId,
                    questionDTO.getQuestionText(),
                    questionDTO.getAnswer(),
                    questionDTO.getOptions(),
                    image
            );
 
            return ResponseEntity.status(201)
                    .body(new ApiResponse<>(201, "Success", "Question created successfully.", createdQuestion));
 
        } catch (Exception e) {
            logger.severe("JSON Parsing Error: " + e.getMessage());
            return ResponseEntity.status(400)
                    .body(new ApiResponse<>(400, "Error", "Invalid JSON format.", null));
        }
    }
    @GetMapping("/tests/{testId}/download")
    @PreAuthorize("hasAnyRole('ADMIN', 'STUDENT')")
    public ResponseEntity<byte[]> downloadQuestionsPdf(@PathVariable UUID testId) {
        byte[] pdfContent = questionService.generateQuestionsPdf(testId);
 
        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=questions.pdf")
                .contentType(MediaType.APPLICATION_PDF)
                .body(pdfContent);
    }

    @GetMapping("/tests/{testId}/questions")
    @PreAuthorize("hasAnyRole('ADMIN','STUDENT')")
    public ResponseEntity<ApiResponse<List<QuestionDTO>>> getQuestionsByTestId(@PathVariable UUID testId) {
        List<QuestionDTO> questions = questionService.getQuestionsByTestId(testId);
        return ResponseEntity.ok(new ApiResponse<>(200, "Success", "Questions retrieved successfully.", questions));
    }
    
   
 
    /**
     * ✅ PUT - Update a question by ID
     */
    @PutMapping(value = "/tests/questions/{questionId}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<QuestionDTO>> updateQuestion(
            @PathVariable UUID questionId,
            @RequestParam("question") String questionJson,
            @RequestParam(value = "image", required = false) MultipartFile image) {
 
        try {
            // ✅ Log the received JSON
            logger.info("Received JSON: " + questionJson);
 
            // ✅ Fix encoding issue (force UTF-8)
            questionJson = new String(questionJson.getBytes(StandardCharsets.ISO_8859_1), StandardCharsets.UTF_8);
 
            // ✅ Parse JSON into QuestionDTO
            QuestionDTO questionDTO = objectMapper.readValue(questionJson, QuestionDTO.class);
 
            // ✅ Call service to update the question
            QuestionDTO updatedQuestion = questionService.updateQuestion(
                    questionId,
                    questionDTO.getQuestionText(),
                    questionDTO.getAnswer(),
                    questionDTO.getOptions(),
                    image
            );
 
            return ResponseEntity.ok(new ApiResponse<>(200, "Success", "Question updated successfully.", updatedQuestion));
 
        } catch (Exception e) {
            logger.severe("JSON Parsing Error: " + e.getMessage());
            return ResponseEntity.status(400)
                    .body(new ApiResponse<>(400, "Error", "Invalid JSON format.", null));
        }
    }
 
    /**
     * ✅ DELETE - Remove a question by ID
     */
    @DeleteMapping("tests/questions/{questionId}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<Void>> deleteQuestion(@PathVariable UUID questionId) {
        try {
            questionService.deleteQuestion(questionId);
            return ResponseEntity.ok(new ApiResponse<>(200, "Success", "Question deleted successfully.", null));
        } catch (Exception e) {
            return ResponseEntity.status(404).body(new ApiResponse<>(404, "Error", e.getMessage(), null));
        }
    }
}