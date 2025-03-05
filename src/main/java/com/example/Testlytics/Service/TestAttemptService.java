package com.example.Testlytics.Service;

import com.example.Testlytics.DTO.TestAttemptDTO;
import com.example.Testlytics.Entity.TestAttempt;
import com.example.Testlytics.Repository.TestAttemptRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class TestAttemptService {

    @Autowired
    private TestAttemptRepository testAttemptRepository;

    @Autowired
    private TestDetailsService testService; // Assuming a service to fetch test details

    // Convert Entity to DTO
    private TestAttemptDTO convertToDTO(TestAttempt testAttempt) {
        String testDetails = testService.fetchTestDetailsById(testAttempt.getTestId());  // Fetching test details

        return new TestAttemptDTO(
                testAttempt.getAttemptId(),
                testAttempt.getTestId(),
                testDetails,  // Added test details
                testAttempt.getUserId(),
                testAttempt.getScore(),
                testAttempt.getAttemptStartTime(),
                testAttempt.getAttemptEndTime(),
                testAttempt.getFeedback(),
                testAttempt.getQuery()
        );
    }

    // Convert DTO to Entity (testDetails is not needed when saving)
    private TestAttempt convertToEntity(TestAttemptDTO dto) {
        TestAttempt testAttempt = new TestAttempt();
        testAttempt.setAttemptId(dto.getAttemptId());
        testAttempt.setTestId(dto.getTestId());
        testAttempt.setUserId(dto.getUserId());
        testAttempt.setScore(dto.getScore());
        testAttempt.setAttemptStartTime(dto.getAttemptStartTime());
        testAttempt.setAttemptEndTime(dto.getAttemptEndTime());
        testAttempt.setFeedback(dto.getFeedback());
        testAttempt.setQuery(dto.getQuery());
        return testAttempt;
    }

    public TestAttemptDTO createTestAttempt(TestAttemptDTO dto) {
        TestAttempt testAttempt = convertToEntity(dto);
        TestAttempt savedAttempt = testAttemptRepository.save(testAttempt);
        return convertToDTO(savedAttempt);
    }

    public TestAttemptDTO getTestAttemptById(Long id) {
        Optional<TestAttempt> attempt = testAttemptRepository.findById(id);
        return attempt.map(this::convertToDTO).orElse(null);
    }

    public List<TestAttemptDTO> getAllTestAttempts() {
        return testAttemptRepository.findAll().stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    public List<TestAttemptDTO> getTestAttemptsByUserId(Long userId) {
        return testAttemptRepository.findByUserId(userId).stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    public List<TestAttemptDTO> getTestAttemptsByTestId(Long testId) {
        return testAttemptRepository.findByTestId(testId).stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    public void deleteTestAttempt(Long id) {
        testAttemptRepository.deleteById(id);
    }
}
