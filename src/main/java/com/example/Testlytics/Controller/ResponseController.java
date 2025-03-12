package com.example.Testlytics.Controller;

import com.example.Testlytics.DTO.ApiResponse;
import com.example.Testlytics.DTO.ResponseDTO;
import com.example.Testlytics.Entity.Response;
import com.example.Testlytics.Service.ResponseService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/responses")
public class ResponseController {

    private final ResponseService responseService;

    public ResponseController(ResponseService responseService) {
        this.responseService = responseService;
    }

    @PostMapping
    public ResponseEntity<ApiResponse<Response>> submitResponse(@RequestBody ResponseDTO.SubmitResponse request) {
        Response response = responseService.submitResponse(request);

        ApiResponse<Response> apiResponse = new ApiResponse<>(
                201, "Created", "Response submitted successfully", response);

        return ResponseEntity.status(201).body(apiResponse);
    }

    @GetMapping("/{testId}")
    public ResponseEntity<ApiResponse<List<Response>>> getResponsesByTestId(@PathVariable UUID testId) {
        List<Response> responses = responseService.getResponsesByTestId(testId);

        ApiResponse<List<Response>> apiResponse = new ApiResponse<>(
                200, "OK", "Responses retrieved successfully", responses);

        return ResponseEntity.ok(apiResponse);
    }
}
