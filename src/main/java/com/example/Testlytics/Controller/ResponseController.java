package com.example.Testlytics.Controller;

import com.example.Testlytics.Entity.Response;
import com.example.Testlytics.Service.ResponseService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("/api/responses")
public class ResponseController {

    private final ResponseService responseService;

    public ResponseController(ResponseService responseService) {
        this.responseService = responseService;
    }

    @PostMapping
    public ResponseEntity<Response> submitResponse(@RequestBody Response response) {
        Response savedResponse = responseService.saveResponse(response);
        return ResponseEntity.status(201).body(savedResponse);
    }

    @GetMapping("/{responseId}")
    public ResponseEntity<Response> getResponse(@PathVariable Long responseId) {
        Optional<Response> response = responseService.getResponse(responseId);
        return response.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }
}
