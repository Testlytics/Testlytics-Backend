package com.example.Testlytics.Controller;

import com.example.Testlytics.DTO.ApiResponse;
import com.example.Testlytics.DTO.TestDTO;
import com.example.Testlytics.Service.TestService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;



@RestController
@RequestMapping("/api/tests")
public class TestController {

    @Autowired
    private TestService testService;

    // ✅ GET ALL TESTS
    @GetMapping
    @PreAuthorize("hasAnyRole('STUDENT', 'ADMIN')")
    public ResponseEntity<ApiResponse<List<TestDTO>>> getAllTests() {
        List<TestDTO> tests = testService.getAllTests();
        return ResponseEntity.ok(new ApiResponse<>(200, "OK", "All tests retrieved successfully", tests));
    }

    // ✅ GET TEST BY ID
    // @GetMapping("/{testId}")
    // @PreAuthorize("hasAnyRole('STUDENT', 'ADMIN')")
    // public ResponseEntity<ApiResponse<TestDTO>> getTestById(@PathVariable UUID testId) {
    //     TestDTO test = testService.getTestById(testId);
    //     return ResponseEntity.ok(new ApiResponse<>(200, "OK", "Test retrieved successfully", test));
    // }
    @GetMapping("/upcoming")
    @PreAuthorize("hasAnyRole('STUDENT', 'ADMIN')")
    public ResponseEntity<ApiResponse<List<TestDTO>>> getUpcomingTests() {
        List<TestDTO> upcomingTests = testService.getUpcomingTests();
        return ResponseEntity.ok(new ApiResponse<>(200, "OK", "Upcoming tests retrieved successfully", upcomingTests));
    }
    @GetMapping("/history")
    @PreAuthorize("hasAnyRole('STUDENT', 'ADMIN')")
    public ResponseEntity<ApiResponse<List<TestDTO>>> getCompletedTests() {
        List<TestDTO> completedTests = testService.getCompletedTests();
        return ResponseEntity.ok(new ApiResponse<>(200, "OK", "Completed tests retrieved successfully", completedTests));
    }



    // ✅ CREATE TEST (with conflict validation)
    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<TestDTO>> createTest(@RequestBody TestDTO testDTO) {
        try {
            TestDTO createdTest = testService.createTest(testDTO);
            return ResponseEntity.status(HttpStatus.CREATED)
                    .body(new ApiResponse<>(201, "Created", "Test created successfully", createdTest));
        } catch (IllegalStateException e) {
            return ResponseEntity.status(HttpStatus.CONFLICT)
                    .body(new ApiResponse<>(409, "Conflict", e.getMessage(), null));
        }
    }

    // ✅ UPDATE TEST
    @PutMapping("/{testId}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<TestDTO>> updateTest(@PathVariable UUID testId, @RequestBody TestDTO testDTO) {
        TestDTO updatedTest = testService.updateTest(testId, testDTO);
        return ResponseEntity.ok(new ApiResponse<>(200, "OK", "Test updated successfully", updatedTest));
    }

    // ✅ DELETE TEST
    @DeleteMapping("/{testId}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<Void>> deleteTest(@PathVariable UUID testId) {
        testService.deleteTest(testId);
        return ResponseEntity.ok(new ApiResponse<>(200, "Success", "Test deleted successfully", null));
    }
}
