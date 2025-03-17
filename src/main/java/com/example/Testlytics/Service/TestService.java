package com.example.Testlytics.Service;

import com.example.Testlytics.DTO.TestDTO;
import com.example.Testlytics.Entity.Test;
import com.example.Testlytics.Repository.TestRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class TestService {

    @Autowired
    private TestRepository testRepository;

    // ✅ GET ALL TESTS
    public List<TestDTO> getAllTests() {
    List<Test> tests = testRepository.findAllActiveTests()
            .stream()
            .filter(test -> test.getTestDate().isAfter(LocalDate.now()) ||
                    (test.getTestDate().isEqual(LocalDate.now()) && test.getEndTime().isAfter(LocalTime.now())))
            .collect(Collectors.toList());
    return tests.stream().map(this::convertToDTO).collect(Collectors.toList());
}


    // ✅ GET TEST BY ID
    public TestDTO getTestById(UUID testId) {
        Optional<Test> test = testRepository.findActiveTestById(testId);
        return test.map(this::convertToDTO).orElse(null);
    }
    public List<TestDTO> getUpcomingTests() {
        List<Test> upcomingTests = testRepository.findUpcomingTests();
        return upcomingTests.stream().map(this::convertToDTO).collect(Collectors.toList());
    }
    public List<TestDTO> getCompletedTests() {
        List<Test> completedTests = testRepository.findCompletedTests();
        return completedTests.stream().map(this::convertToDTO).collect(Collectors.toList());
    }
    
    

    // ✅ CREATE TEST
    public TestDTO createTest(TestDTO testDTO) {
        Test test = convertToEntity(testDTO);
        Test savedTest = testRepository.save(test);
        return convertToDTO(savedTest);
    }

    // ✅ UPDATE TEST
    public TestDTO updateTest(UUID testId, TestDTO testDTO) {
        Optional<Test> optionalTest = testRepository.findActiveTestById(testId);
        if (optionalTest.isPresent()) {
            Test existingTest = optionalTest.get();
            existingTest.setTestName(testDTO.getTestName());
            existingTest.setTestDate(testDTO.getTestDate());
            existingTest.setTestDuration(testDTO.getTestDuration());
            existingTest.setStartTime(testDTO.getStartTime());
            existingTest.setEndTime(testDTO.getEndTime());
            Test updatedTest = testRepository.save(existingTest);
            return convertToDTO(updatedTest);
        }
        return null;
    }

    // ✅ DELETE TEST
    public void deleteTest(UUID testId) {
        Optional<Test> optionalTest = testRepository.findById(testId);
        if (optionalTest.isPresent()) {
            Test test = optionalTest.get();
            test.setDeletedOn(LocalDateTime.now());
            testRepository.save(test);
        }
    }

    // ✅ Convert Entity to DTO
    private TestDTO convertToDTO(Test test) {
        return TestDTO.builder()
                .testId(test.getTestId())
                .subjectId(test.getSubjectId())
                .testName(test.getTestName())
                .testDate(test.getTestDate())
                .testDuration(test.getTestDuration())
                .startTime(test.getStartTime())
                .endTime(test.getEndTime())
                .isActive(test.isActive())  // ✅ Now includes `isActive` status
                .build();
    }

    // ✅ Convert DTO to Entity
    private Test convertToEntity(TestDTO testDTO) {
        return Test.builder()
                .testId(testDTO.getTestId())
                .subjectId(testDTO.getSubjectId())
                .testName(testDTO.getTestName())
                .testDate(testDTO.getTestDate())
                .testDuration(testDTO.getTestDuration())
                .startTime(testDTO.getStartTime())
                .endTime(testDTO.getEndTime())
                .build();
    }
}
