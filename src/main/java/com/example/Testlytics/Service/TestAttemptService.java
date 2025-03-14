package com.example.Testlytics.Service;

import com.example.Testlytics.DTO.ApiResponse;
import com.example.Testlytics.DTO.TestAttemptDTO.*;
import com.example.Testlytics.Entity.TestAttempt;
import com.example.Testlytics.Entity.TestAttemptId;
import com.example.Testlytics.Repository.OutcomeRepository;
import com.example.Testlytics.Repository.TestAttemptRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

@Service
public class TestAttemptService {
    private final TestAttemptRepository repository;
    private final OutcomeRepository outcomeRepository; // Renamed from ResponseRepository to OutcomeRepository

    public TestAttemptService(TestAttemptRepository repository, OutcomeRepository outcomeRepository) {
        this.repository = repository;
        this.outcomeRepository = outcomeRepository;
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

        // ✅ Calculate score based on correct outcomes (renamed from responses)
        long correctAnswers = outcomeRepository.countByTestIdAndUserIdAndIsCorrectTrue(testId, userId);

        attempt.setScore((int) correctAnswers); // Each correct answer gives 1 score
        attempt.setCompleted(true);
        attempt.setAttemptEndTime(LocalDateTime.now());
        attempt.setQuery(request.getQuery());
        attempt.setFeedback(request.getFeedback());

        repository.save(attempt);

        return new ApiResponse<>(200, "Success", "Test attempt submitted successfully", "Final score: " + correctAnswers);
    }

    public ApiResponse<TestAttempt> getTestAttempt(UUID testId, Integer userId) {
        TestAttemptId attemptId = new TestAttemptId(testId, userId);

        Optional<TestAttempt> attemptOpt = repository.findById(attemptId);
        if (attemptOpt.isEmpty()) {
            return new ApiResponse<>(404, "Error", "Test attempt not found", null);
        }

        return new ApiResponse<>(200, "Success", "Test attempt found", attemptOpt.get());
    }
}
