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
    @PutMapping("/{testId}/{userId}")
    public ResponseEntity<ApiResponse<?>> submitTestAttempt(
            @PathVariable UUID testId,
            @PathVariable Integer userId,
            @RequestBody TestAttemptSubmitRequest request) {

        ApiResponse<?> response = service.submitTestAttempt(testId, userId, request);
        return ResponseEntity.status(response.getStatusCode()).body(response);
    }
}
