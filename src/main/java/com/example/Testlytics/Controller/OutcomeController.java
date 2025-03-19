package com.example.Testlytics.Controller;

import com.example.Testlytics.DTO.ApiResponse;
import com.example.Testlytics.DTO.OutcomeDTO;
import com.example.Testlytics.Entity.Outcome;
import com.example.Testlytics.Service.OutcomeService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/tests/{testId}/outcomes")
public class OutcomeController {

    private final OutcomeService outcomeService;

    public OutcomeController(OutcomeService outcomeService) {
        this.outcomeService = outcomeService;
    }

    @PostMapping
    public ResponseEntity<ApiResponse<Outcome>> submitOutcome(
            @PathVariable UUID testId, @RequestBody OutcomeDTO.SubmitOutcome request) {  // 👈 Removed testId from request body
        Outcome outcome = outcomeService.submitOutcome(testId, request);  // 👈 Pass testId from URL

        ApiResponse<Outcome> apiResponse = new ApiResponse<>(
                201, "Created", "Outcome submitted successfully", outcome);

        return ResponseEntity.status(201).body(apiResponse);
    }

    @GetMapping
    public ResponseEntity<ApiResponse<List<Outcome>>> getOutcomesByTestId(@PathVariable UUID testId) {
        List<Outcome> outcomes = outcomeService.getOutcomesByTestId(testId);

        ApiResponse<List<Outcome>> apiResponse = new ApiResponse<>(
                200, "OK", "Outcomes retrieved successfully", outcomes);

        return ResponseEntity.ok(apiResponse);
    }
}
