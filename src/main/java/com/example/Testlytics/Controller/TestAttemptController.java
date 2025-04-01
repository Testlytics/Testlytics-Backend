package com.example.Testlytics.Controller;

import com.example.Testlytics.DTO.ApiResponse;
import com.example.Testlytics.DTO.TestAttemptDTO.TestAttemptFeedbackRequest;
import com.example.Testlytics.DTO.TestAttemptDTO.TestAttemptSubmitRequest;
import com.example.Testlytics.Service.TestAttemptService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


import java.util.UUID;
 
@RestController
@RequestMapping("/api/attempts")
public class TestAttemptController {
    private final TestAttemptService service;

    public TestAttemptController(TestAttemptService service) {
        this.service = service;
    }

    /**
     * Start a test attempt.
     */
    @PostMapping("/start")
    public ResponseEntity<ApiResponse<?>> startTestAttempt(
            @RequestParam UUID testId,
            @RequestParam Integer userId) {

        ApiResponse<?> response = service.startTestAttempt(testId, userId);
        return ResponseEntity.status(response.getStatusCode()).body(response);
    }

    /*
     Submit a test attempt.
     */
    @PutMapping("/submit")
    public ResponseEntity<ApiResponse<?>> submitTestAttempt(
            @RequestParam UUID testId,
            @RequestParam Integer userId,
            @RequestBody TestAttemptSubmitRequest request) {

        ApiResponse<?> response = service.submitTestAttempt(testId, userId, request);
        return ResponseEntity.status(response.getStatusCode()).body(response);
    }

    @PutMapping("/feedback")
public ResponseEntity<ApiResponse<?>> addTeacherFeedback(
        @RequestParam UUID testId,
        @RequestParam Integer userId,
        @RequestBody TestAttemptFeedbackRequest request) {

    ApiResponse<?> response = service.addTeacherFeedback(testId, userId, request);
    return ResponseEntity.status(response.getStatusCode()).body(response);
}


    /**
     * Get a user's test attempt details.
     */
    @GetMapping("/{testId}/user/{userId}")
    public ResponseEntity<ApiResponse<?>> getTestAttempt(
            @PathVariable UUID testId,
            @PathVariable Integer userId) {
        ApiResponse<?> response = service.getTestAttempt(testId, userId);
        return ResponseEntity.status(response.getStatusCode()).body(response);
    }

    /**
     * Get attendance report - List of tests a user has attempted.
     */
    @GetMapping("/user/{userId}/attendance")
    public ResponseEntity<ApiResponse<?>> getUserAttendance(@PathVariable Integer userId) {
        ApiResponse<?> response = service.getUserAttendance(userId);
        return ResponseEntity.status(response.getStatusCode()).body(response);
    }

    /**
     * Get the list of students who attended a specific test.
     */
    @GetMapping("/test/{testId}/students")
    public ResponseEntity<ApiResponse<?>> getStudentsByTest(@PathVariable UUID testId) {
        ApiResponse<?> response = service.getStudentsByTestId(testId);
        return ResponseEntity.status(response.getStatusCode()).body(response);
    }

    /**
     * Get the average score for a subject.
     */
    @GetMapping("/subjects/{subjectId}/average")
    public ResponseEntity<ApiResponse<Double>> getAverageScore(@PathVariable UUID subjectId) {
        Double averageScore = service.getAverageScoreBySubject(subjectId).orElse(0.0);
        return ResponseEntity.ok(new ApiResponse<>(200, "Success", "Average score retrieved", averageScore));
    }
}
