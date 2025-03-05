package com.example.Testlytics.Controller;

import com.example.Testlytics.DTO.TestDetailsDTO;
import com.example.Testlytics.Service.TestDetailsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/testdetails")
public class TestDetailsController {

    @Autowired
    private TestDetailsService testDetailsService;

    // ✅ GET ALL TESTS - Simply call the service
    @GetMapping
    @PreAuthorize("hasAnyRole('STUDENT', 'ADMIN')")
    public List<TestDetailsDTO> getAllTests() {
        return testDetailsService.getAllTests();
    }

    // ✅ GET TEST BY ID - Simply call the service
    @GetMapping("/{testdetailsId}")
    @PreAuthorize("hasAnyRole('STUDENT', 'ADMIN')")
    public TestDetailsDTO getTestById(@PathVariable UUID testdetailsId) {
        return testDetailsService.getTestById(testdetailsId);
    }

    // ✅ CREATE TEST - Simply call the service
    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public TestDetailsDTO createTest(@RequestBody TestDetailsDTO testDetailsDTO) {
        return testDetailsService.createTest(testDetailsDTO);
    }

    // ✅ UPDATE TEST - Simply call the service
    @PutMapping("/{testdetailsId}")
    @PreAuthorize("hasRole('ADMIN')")
    public TestDetailsDTO updateTest(@PathVariable UUID testdetailsId, @RequestBody TestDetailsDTO testDetailsDTO) {
        return testDetailsService.updateTest(testdetailsId, testDetailsDTO);
    }

    // ✅ DELETE TEST - Simply call the service
    @DeleteMapping("/{testdetailsId}")
    @PreAuthorize("hasRole('ADMIN')")
    public void deleteTest(@PathVariable UUID testdetailsId) {
        testDetailsService.deleteTest(testdetailsId);
    }
}
