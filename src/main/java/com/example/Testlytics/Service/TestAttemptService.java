package com.example.Testlytics.Service;
 
import com.example.Testlytics.DTO.ApiResponse;
import com.example.Testlytics.DTO.TestAttemptDTO.*;
import com.example.Testlytics.Entity.TestAttempt;
import com.example.Testlytics.Entity.TestAttemptId;
import com.example.Testlytics.Repository.OutcomeRepository;
import com.example.Testlytics.Repository.TestAttemptRepository;
import com.example.Testlytics.Repository.TestRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
 
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;
 
@Service
public class TestAttemptService {
    @Autowired
    private TestAttemptRepository repository;
 
    @Autowired
    private OutcomeRepository outcomeRepository; // Renamed from ResponseRepository to OutcomeRepository
 
    @Autowired
    private TestRepository testRepository;
 
    @Autowired
    private TestService testService;
 
    public TestAttemptService() {
        // Default constructor
    }
 
    public TestAttemptService(TestAttemptRepository repository, OutcomeRepository outcomeRepository) {
        this.repository = repository;
        this.outcomeRepository = outcomeRepository;
    }
 
 
    public TestAttemptService(TestAttemptRepository repository, TestRepository testRepository) {
        this.repository = repository;
        this.testRepository = testRepository;
    }
 
    public Optional<Double> getAverageScoreBySubject(UUID subjectId) {
        return repository.findAverageScoreBySubject(subjectId);
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
 
        TestAttempt attempt = new TestAttempt(id, 0, LocalDateTime.now(), null, null, null);
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
        // ✅ Calculate score based on correct outcomes (fixed count query)
        long correctAnswers = outcomeRepository.countByTestIdAndUserIdAndIsCorrect(testId, userId, true);
        // Convert to percentage if needed, or keep as absolute count
        int totalQuestions = (int) outcomeRepository.countByTestIdAndUserId(testId, userId);
        double scorePercentage = totalQuestions > 0 ? 
            ((double) correctAnswers / totalQuestions) * 100 : 0;
        attempt.setScore((int) correctAnswers); // Store absolute correct count
        attempt.setAttemptEndTime(LocalDateTime.now());
        attempt.setQuery(request.getQuery());
        attempt.setFeedback(null);
        repository.save(attempt);
        return new ApiResponse<>(200, "Success", "Test attempt submitted successfully", 
            String.format("Score: %d/%d (%.2f%%)", correctAnswers, totalQuestions, scorePercentage));
    }
 
    public ApiResponse<String> addTeacherFeedback(UUID testId, Integer userId, TestAttemptFeedbackRequest request) {
        TestAttemptId attemptId = new TestAttemptId(testId, userId);
        Optional<TestAttempt> attemptOpt = repository.findById(attemptId);
        if (attemptOpt.isEmpty()) {
            return new ApiResponse<>(404, "Error", "Test attempt not found", null);
        }
        TestAttempt attempt = attemptOpt.get();
        // ✅ Only update the feedback (DO NOT change end time)
        attempt.setFeedback(request.getFeedback());
        repository.save(attempt);
        return new ApiResponse<>(200, "Success", "Feedback added successfully", "Feedback: " + request.getFeedback());
    }

 
    /**
     * Get a test attempt.
     */
    public ApiResponse<TestAttempt> getTestAttempt(UUID testId, Integer userId) {
        TestAttemptId attemptId = new TestAttemptId(testId, userId);
 
        Optional<TestAttempt> attemptOpt = repository.findById(attemptId);
        if (attemptOpt.isEmpty()) {
            return new ApiResponse<>(404, "Error", "Test attempt not found", null);
        }
 
        return new ApiResponse<>(200, "Success", "Test attempt found", attemptOpt.get());
    }
 
    /**
     * Get user attendance - List of test IDs that a user has attempted.
     */
    public ApiResponse<List<UUID>> getUserAttendance(Integer userId) {
        List<TestAttempt> attempts = repository.findTestAttemptsByUserId(userId);
 
        if (attempts.isEmpty()) {
            return new ApiResponse<>(404, "Error", "No attendance records found for this user", null);
        }
 
        // Extract test IDs from attempts
        List<UUID> attendedTests = attempts.stream()
                .map(attempt -> attempt.getId().getTestId())
                .collect(Collectors.toList());
 
        return new ApiResponse<>(200, "Success", "Attendance records found", attendedTests);
    }
 
    /**
     * Get the list of students who have attended a specific test.
     */
    public ApiResponse<List<Integer>> getStudentsByTestId(UUID testId) {
        List<Integer> students = repository.findUsersByTestId(testId); // This will now exclude deleted users
        if (students.isEmpty()) {
            return new ApiResponse<>(404, "Error", "No students attended this test", null);
        }
        return new ApiResponse<>(200, "Success", "Students found", students);
    }
    
 
 
    /**
     * Get missed tests of a user.
     */
    public ApiResponse<List<UUID>> getMissedTests(Integer userId) {
        List<UUID> allTests = testRepository.findAllTestIds(); // Get all test IDs
        List<UUID> attendedTests = repository.findTestAttemptsByUserId(userId).stream()
                .map(attempt -> attempt.getId().getTestId())
                .collect(Collectors.toList());
 
        // Find missed tests (allTests - attendedTests)
        List<UUID> missedTests = allTests.stream()
                .filter(testId -> !attendedTests.contains(testId))
                .collect(Collectors.toList());
 
        if (missedTests.isEmpty()) {
            return new ApiResponse<>(200, "Success", "User has attended all tests", null);
        }
 
        return new ApiResponse<>(200, "Success", "Missed tests found", missedTests);
    }
 
 
}