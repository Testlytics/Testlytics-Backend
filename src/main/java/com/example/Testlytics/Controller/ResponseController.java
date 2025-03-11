package com.example.Testlytics.Controller;

import com.example.Testlytics.DTO.ResponseDTO;
import com.example.Testlytics.Entity.Response;
import com.example.Testlytics.Service.ResponseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/responses")
public class ResponseController {

    @Autowired
    private ResponseService responseService;

    // Submit a response
    @PostMapping
    public ResponseDTO submitResponse(@RequestBody Response response) {
        return responseService.saveResponse(response);
    }

    // Get responses by testId
    @GetMapping("/{testId}")
    public List<ResponseDTO> getResponses(@PathVariable Long testId) {
        return responseService.getResponsesByTestId(testId);
    }
}
