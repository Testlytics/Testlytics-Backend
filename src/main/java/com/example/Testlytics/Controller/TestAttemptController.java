package com.example.Testlytics.Controller;

import com.example.Testlytics.DTO.ApiResponse;
import com.example.Testlytics.DTO.TestAttemptDTO.TestAttemptStartRequest;
import com.example.Testlytics.DTO.TestAttemptDTO.TestAttemptSubmitRequest;
import com.example.Testlytics.Service.TestAttemptService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


import java.util.UUID;

@RestController
@RequestMapping("/api/test-attempts")
public class TestAttemptController {

    private final TestAttemptService service;

    public TestAttemptController(TestAttemptService service) {
        this.service = service;
    }

    /**
     * Start a test attempt.
     */
    @PostMapping
    public ResponseEntity<ApiResponse<?>> startTestAttempt(@RequestBody TestAttemptStartRequest request) {
        System.out.println("Received testId: " + request.getTestId());
        System.out.println("Received userId: " + request.getUserId());

        ApiResponse<?> response = service.startTestAttempt(request.getTestId(), request.getUserId());
        return ResponseEntity.status(response.getStatusCode()).body(response);
    }

    /**
     * Submit a test attempt.
     */
    @PutMapping("/test/{testId}/user/{userId}")
    public ResponseEntity<ApiResponse<?>> submitTestAttempt(
            @PathVariable UUID testId,
            @PathVariable Integer userId,
            @RequestBody TestAttemptSubmitRequest request) {

        ApiResponse<?> response = service.submitTestAttempt(testId, userId, request);
        return ResponseEntity.status(response.getStatusCode()).body(response);
    }

    // /**
    //  * Get a test attempt.
    //  */
    // @GetMapping("/{testId}/{userId}")
    // public ResponseEntity<ApiResponse<?>> getTestAttempt(
    //         @PathVariable UUID testId,
    //         @PathVariable Integer userId) {
    //     ApiResponse<?> response = service.getTestAttempt(testId, userId);
    //     return ResponseEntity.status(response.getStatusCode()).body(response);
    // }

    /**
     * Get attendance report - List of tests a user has attended.
     */
    @GetMapping("/attendance/user/{userId}")
    public ResponseEntity<ApiResponse<?>> getUserAttendance(@PathVariable Integer userId) {
        ApiResponse<?> response = service.getUserAttendance(userId);
        return ResponseEntity.status(response.getStatusCode()).body(response);
    }

     /**
     * Get the list of students who attended a specific test.
     */
    @GetMapping("/attendance/test/{testId}")
    public ResponseEntity<ApiResponse<?>> getStudentsByTest(@PathVariable String testId) {
        try {
            UUID uuid = UUID.fromString(testId); // Validate UUID format
            ApiResponse<?> response = service.getStudentsByTestId(uuid);
            return ResponseEntity.status(response.getStatusCode()).body(response);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(new ApiResponse<>(400, "Error", "Invalid UUID format", null));
        }
    }
    
}
