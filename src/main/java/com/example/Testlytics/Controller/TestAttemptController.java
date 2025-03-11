package com.example.Testlytics.Controller;

import com.example.Testlytics.DTO.TestAttemptDTO;
import com.example.Testlytics.Service.TestAttemptService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/test-attempts")
public class TestAttemptController {

    @Autowired
    private TestAttemptService testAttemptService;

    @PostMapping
    public ResponseEntity<TestAttemptDTO> createTestAttempt(@RequestBody TestAttemptDTO dto) {
        TestAttemptDTO createdAttempt = testAttemptService.createTestAttempt(dto);
        return ResponseEntity.ok(createdAttempt);
    }

    @GetMapping("/{id}")
    public ResponseEntity<TestAttemptDTO> getTestAttemptById(@PathVariable Long id) {
        TestAttemptDTO attempt = testAttemptService.getTestAttemptById(id);
        return (attempt != null) ? ResponseEntity.ok(attempt) : ResponseEntity.notFound().build();
    }

    @GetMapping
    public ResponseEntity<List<TestAttemptDTO>> getAllTestAttempts() {
        return ResponseEntity.ok(testAttemptService.getAllTestAttempts());
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<List<TestAttemptDTO>> getTestAttemptsByUserId(@PathVariable Long userId) {
        return ResponseEntity.ok(testAttemptService.getTestAttemptsByUserId(userId));
    }

    @GetMapping("/test/{testId}")
    public ResponseEntity<List<TestAttemptDTO>> getTestAttemptsByTestId(@PathVariable Long testId) {
        return ResponseEntity.ok(testAttemptService.getTestAttemptsByTestId(testId));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteTestAttempt(@PathVariable Long id) {
        testAttemptService.deleteTestAttempt(id);
        return ResponseEntity.noContent().build();
    }
}
