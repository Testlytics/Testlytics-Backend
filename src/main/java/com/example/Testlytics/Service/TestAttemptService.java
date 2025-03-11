package com.example.Testlytics.Service;

import com.example.Testlytics.DTO.ApiResponse;
import com.example.Testlytics.DTO.TestAttemptDTO.*;
import com.example.Testlytics.Entity.TestAttempt;
import com.example.Testlytics.Entity.TestAttemptId;
import com.example.Testlytics.Repository.TestAttemptRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

@Service
public class TestAttemptService {
    private final TestAttemptRepository repository;

    public TestAttemptService(TestAttemptRepository repository) {
        this.repository = repository;
    }

    /**
     * Start a test attempt.
     */
    public ApiResponse<TestAttemptId> startTestAttempt(UUID testId, Integer userId) {
        if (testId == null || userId == null) {
            throw new IllegalArgumentException("testId and userId are required");
        }

        TestAttemptId id = new TestAttemptId(testId, userId);

        // Check if an attempt already exists
        if (repository.existsById(id)) {
            return new ApiResponse<>(400, "Error", "Test attempt already exists", null);
        }

        TestAttempt attempt = new TestAttempt(id, 0, false, LocalDateTime.now(), null, null, null);
        repository.save(attempt);

        return new ApiResponse<>(201, "Success", "Test attempt started", id);
    }

    /**
     * Submit a test attempt.
     */
    public ApiResponse<String> submitTestAttempt(UUID testId, Integer userId, TestAttemptSubmitRequest request) {
        TestAttemptId attemptId = new TestAttemptId(testId, userId);

        Optional<TestAttempt> attemptOpt = repository.findById(attemptId);
        if (attemptOpt.isEmpty()) {
            return new ApiResponse<>(404, "Error", "Test attempt not found", null);
        }

        TestAttempt attempt = attemptOpt.get();
        attempt.setScore(request.getScore());
        attempt.setCompleted(true);
        attempt.setAttemptEndTime(LocalDateTime.now());
        attempt.setQuery(request.getQuery());
        attempt.setFeedback(request.getFeedback());

        repository.save(attempt);

        return new ApiResponse<>(200, "Success", "Test attempt submitted successfully", "Submission recorded");
    }
}
