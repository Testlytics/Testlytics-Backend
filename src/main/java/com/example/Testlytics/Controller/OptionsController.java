package com.example.Testlytics.Controller;


import com.example.Testlytics.DTO.ApiResponse;
import com.example.Testlytics.DTO.OptionsDTO;
import com.example.Testlytics.Service.OptionsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.UUID;

@RestController
@RequestMapping("/api/options")


public class OptionsController {

    @Autowired
    private OptionsService optionsService;



    @PostMapping("/{questionId}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<OptionsDTO>> createOption(
            @PathVariable UUID questionId,
            @RequestBody Map<String, Object> requestBody) {

        String optionText = (String) requestBody.get("optionText");
        boolean isCorrect = (Boolean) requestBody.get("isCorrect");

        OptionsDTO createdOption = optionsService.createOption(questionId, optionText, isCorrect);

        ApiResponse<OptionsDTO> response = new ApiResponse<>(
                201, "Success", "Option created successfully.", createdOption);

        return ResponseEntity.status(201).body(response);
    }

    @GetMapping("/questions/{questionId}")
    @PreAuthorize("hasAnyRole('ADMIN','STUDENT')")
    public ResponseEntity<ApiResponse<List<OptionsDTO>>> getOptionsByQuestionId(@PathVariable UUID questionId) {
        List<OptionsDTO> options = optionsService.getOptionsByQuestionId(questionId);

        ApiResponse<List<OptionsDTO>> response = new ApiResponse<>(
                200, "Success", "Options retrieved successfully.", options);

        return ResponseEntity.ok(response);
    }

    @PutMapping("/{optionId}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<OptionsDTO>> updateOption(
            @PathVariable UUID optionId,
            @RequestBody Map<String, Object> requestBody) {

        String optionText = (String) requestBody.get("optionText");
        boolean isCorrect = (Boolean) requestBody.get("isCorrect");

        OptionsDTO updatedOption = optionsService.updateOption(optionId, optionText, isCorrect);

        ApiResponse<OptionsDTO> response = new ApiResponse<>(
                200, "Success", "Option updated successfully.", updatedOption);

        return ResponseEntity.ok(response);
    }

    @GetMapping("/{optionId}")
    @PreAuthorize("hasAnyRole('ADMIN','STUDENT')")
    public ResponseEntity<ApiResponse<OptionsDTO>> getOptionById(@PathVariable UUID optionId) {
        OptionsDTO option = optionsService.getOptionById(optionId);

        ApiResponse<OptionsDTO> response = new ApiResponse<>(200, "Success", "Option retrieved successfully.", option);
        return ResponseEntity.ok(response);
    }


    @DeleteMapping("/{optionId}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<String>> deleteOption(@PathVariable UUID optionId) {
        optionsService.deleteOption(optionId);

        ApiResponse<String> response = new ApiResponse<>(200, "Success", "Option deleted successfully.", null);
        return ResponseEntity.ok(response);
    }


}
