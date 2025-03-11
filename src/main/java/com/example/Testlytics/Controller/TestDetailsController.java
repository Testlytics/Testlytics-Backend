package com.example.Testlytics.Controller;

import com.example.Testlytics.DTO.ApiResponse;
import com.example.Testlytics.DTO.TestDetailsDTO;
import com.example.Testlytics.Service.TestDetailsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/testdetails")
public class TestDetailsController {

    @Autowired
    private TestDetailsService testDetailsService;

    // ✅ GET ALL TESTS
    @GetMapping
    @PreAuthorize("hasAnyRole('STUDENT', 'ADMIN')")
    public ResponseEntity<ApiResponse<List<TestDetailsDTO>>> getAllTests() {
        List<TestDetailsDTO> tests = testDetailsService.getAllTests();
        return ResponseEntity.ok(new ApiResponse<>(200, "OK", "All tests retrieved successfully", tests));
    }

    // ✅ GET TEST BY ID
    @GetMapping("/{testdetailsId}")
    @PreAuthorize("hasAnyRole('STUDENT', 'ADMIN')")
    public ResponseEntity<ApiResponse<TestDetailsDTO>> getTestById(@PathVariable UUID testdetailsId) {
        TestDetailsDTO test = testDetailsService.getTestById(testdetailsId);
        return ResponseEntity.ok(new ApiResponse<>(200, "OK", "Test retrieved successfully", test));
    }

    // ✅ CREATE TEST
    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<TestDetailsDTO>> createTest(@RequestBody TestDetailsDTO testDetailsDTO) {
        TestDetailsDTO createdTest = testDetailsService.createTest(testDetailsDTO);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(new ApiResponse<>(201, "Created", "Test created successfully", createdTest));
    }

    // ✅ UPDATE TEST
    @PutMapping("/{testdetailsId}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<TestDetailsDTO>> updateTest(@PathVariable UUID testdetailsId, @RequestBody TestDetailsDTO testDetailsDTO) {
        TestDetailsDTO updatedTest = testDetailsService.updateTest(testdetailsId, testDetailsDTO);
        return ResponseEntity.ok(new ApiResponse<>(200, "OK", "Test updated successfully", updatedTest));
    }

    // ✅ DELETE test (Soft Delete)
    @DeleteMapping("/{testId}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<Void>> deleteTest(@PathVariable UUID testId) {
        testDetailsService.deleteTest(testId);
        return ResponseEntity.ok(new ApiResponse<>(200, "Success", "Test deleted successfully", null));
    }
}
