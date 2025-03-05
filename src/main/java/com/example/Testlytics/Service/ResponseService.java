package com.example.Testlytics.Service;

import com.example.Testlytics.DTO.ResponseDTO;
import com.example.Testlytics.Entity.Response;
import com.example.Testlytics.Repository.ResponseRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ResponseService {

    @Autowired
    private ResponseRepository responseRepository;

    // Convert Entity to DTO
    private ResponseDTO convertToDTO(Response response) {
        return new ResponseDTO(
                response.getResponseId(),
                response.getTest().getTestId(),
                response.getUser().getUserId(),
                response.getQuestion().getQuestionId(),
                response.getSelectedOptionId(),
                response.getIsCorrect()
        );
    }

    // Submit a response
    public ResponseDTO saveResponse(Response response) {
        Response savedResponse = responseRepository.save(response);
        return convertToDTO(savedResponse);
    }

    // Get responses for a test
    public List<ResponseDTO> getResponsesByTestId(Long testId) {
        return responseRepository.findByTestTestId(testId)
                .stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }
}
