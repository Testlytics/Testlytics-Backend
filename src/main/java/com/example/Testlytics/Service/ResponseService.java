package com.example.Testlytics.Service;

import com.example.Testlytics.DTO.ResponseDTO;
import com.example.Testlytics.Entity.Response;
import com.example.Testlytics.Repository.ResponseRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class ResponseService {

    private final ResponseRepository responseRepository;

    public ResponseService(ResponseRepository responseRepository) {
        this.responseRepository = responseRepository;
    }

    public Response submitResponse(ResponseDTO.SubmitResponse request) {
        Response response = new Response();
        response.setTestId(request.getTestId());
        response.setQuestionId(request.getQuestionId());
        response.setUserId(request.getUserId());
        response.setSelectedOptionId(request.getSelectedOptionId());
        response.setIsCorrect(request.getIsCorrect());

        return responseRepository.save(response);
    }

    public List<Response> getResponsesByTestId(UUID testId) {
        return responseRepository.findByTestId(testId);
    }
}
