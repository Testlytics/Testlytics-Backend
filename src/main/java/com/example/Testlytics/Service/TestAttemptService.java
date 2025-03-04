package com.example.Testlytics.Service;

import com.example.Testlytics.Entity.TestAttempt;
import com.example.Testlytics.Entity.TestAttemptId;
import com.example.Testlytics.Repository.TestAttemptRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;
import java.util.Optional;

@Service
public class TestAttemptService {

    @Autowired
    private TestAttemptRepository testAttemptRepository;

    public TestAttempt startTestAttempt(TestAttempt testAttempt) {
        testAttempt.setAttemptStartTime(LocalDateTime.now());
        return testAttemptRepository.save(testAttempt);
    }

    public Optional<TestAttempt> getTestAttemptById(TestAttemptId attemptId) {
        return testAttemptRepository.findById(attemptId);
    }

    public TestAttempt submitTestAttempt(TestAttemptId attemptId, TestAttempt updatedAttempt) {
        return testAttemptRepository.findById(attemptId).map(existingAttempt -> {
            existingAttempt.setScore(updatedAttempt.getScore());
            existingAttempt.setAttemptEndTime(updatedAttempt.getAttemptEndTime());
            existingAttempt.setFeedback(updatedAttempt.getFeedback());
            existingAttempt.setQuery(updatedAttempt.getQuery());
            return testAttemptRepository.save(existingAttempt);
        }).orElseThrow(() -> new RuntimeException("Test attempt not found"));
    }
}