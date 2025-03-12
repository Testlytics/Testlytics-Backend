package com.example.Testlytics.Service;

import com.example.Testlytics.DTO.ResponseDTO;
import com.example.Testlytics.Entity.Options;
import com.example.Testlytics.Entity.Response;
import com.example.Testlytics.Repository.OptionsRepository;
import com.example.Testlytics.Repository.ResponseRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class ResponseService {

    private final ResponseRepository responseRepository;
    private final OptionsRepository optionsRepository;

    public ResponseService(ResponseRepository responseRepository, OptionsRepository optionsRepository) {
        this.responseRepository = responseRepository;
        this.optionsRepository = optionsRepository;
    }

    public Response submitResponse(ResponseDTO.SubmitResponse request) {
        // Fetch the selected option from the database
        Options selectedOption = optionsRepository.findById(request.getSelectedOptionId())
                .orElseThrow(() -> new RuntimeException("Option not found with id: " + request.getSelectedOptionId()));

        // Create a new Response entity
        Response response = new Response();
        response.setTestId(request.getTestId());
        response.setQuestionId(request.getQuestionId());
        response.setUserId(request.getUserId());
        response.setSelectedOptionId(request.getSelectedOptionId());

        // Set isCorrect based on the selected option's correctness
        response.setIsCorrect(selectedOption.isCorrect());

        // Save response
        return responseRepository.save(response);
    }

    public List<Response> getResponsesByTestId(UUID testId) {
        return responseRepository.findByTestId(testId);
    }
}
