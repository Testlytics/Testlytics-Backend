package com.example.Testlytics.Service;

import com.example.Testlytics.DTO.TestDetailsDTO;
import com.example.Testlytics.Entity.TestDetails;
import com.example.Testlytics.Repository.TestDetailsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class TestDetailsService {

    @Autowired
    private TestDetailsRepository testDetailsRepository;

    // ✅ Convert Entity List to DTO List inside service
    public List<TestDetailsDTO> getAllTests() {
        List<TestDetails> testList = testDetailsRepository.findAllActiveTests();
        return testList.stream().map(this::convertToDTO).collect(Collectors.toList());
    }

    // ✅ Convert Entity to DTO inside service
    public TestDetailsDTO getTestById(UUID testId) {
        TestDetails test = testDetailsRepository.findActiveTestById(testId)
                .orElseThrow(() -> new RuntimeException("Test not found"));
        return convertToDTO(test);
    }

    // ✅ Convert DTO to Entity before saving
    public TestDetailsDTO createTest(TestDetailsDTO testDetailsDTO) {
        TestDetails test = convertToEntity(testDetailsDTO);
        TestDetails savedTest = testDetailsRepository.save(test);
        return convertToDTO(savedTest);
    }

    // ✅ Convert DTO to Entity before updating
    public TestDetailsDTO updateTest(UUID testId, TestDetailsDTO testDetailsDTO) {
        TestDetails existingTest = testDetailsRepository.findActiveTestById(testId)
                .orElseThrow(() -> new RuntimeException("Test not found"));

        existingTest.setSubjectId(testDetailsDTO.getSubjectId());
        existingTest.setTestName(testDetailsDTO.getTestName());
        existingTest.setTestDate(testDetailsDTO.getTestDate());
        existingTest.setTestDuration(testDetailsDTO.getTestDuration());
        existingTest.setStartTime(testDetailsDTO.getStartTime());
        existingTest.setEndTime(testDetailsDTO.getEndTime());

        TestDetails updatedTest = testDetailsRepository.save(existingTest);
        return convertToDTO(updatedTest);
    }

    // ✅ Delete test
    public void deleteTest(UUID testId) {
        testDetailsRepository.deleteById(testId);
    }

    // ✅ Conversion Methods
    private TestDetailsDTO convertToDTO(TestDetails test) {
        return new TestDetailsDTO(
                test.getTestId(),
                test.getSubjectId(),
                test.getTestName(),
                test.getTestDate(),
                test.getTestDuration(),
                test.getStartTime(),
                test.getEndTime()
        );
    }

    private TestDetails convertToEntity(TestDetailsDTO dto) {
        return TestDetails.builder()
                .testId(dto.getTestId())
                .subjectId(dto.getSubjectId())
                .testName(dto.getTestName())
                .testDate(dto.getTestDate())
                .testDuration(dto.getTestDuration())
                .startTime(dto.getStartTime())
                .endTime(dto.getEndTime())
                .build();
    }
}
