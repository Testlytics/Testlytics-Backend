package com.example.Testlytics.Service;

import com.example.Testlytics.Entity.Response;
import com.example.Testlytics.Repository.ResponseRepository;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class ResponseService {

    private final ResponseRepository responseRepository;

    public ResponseService(ResponseRepository responseRepository) {
        this.responseRepository = responseRepository;
    }

    public Response saveResponse(Response response) {
        return responseRepository.save(response);
    }

    public Optional<Response> getResponse(Long responseId) {
        return responseRepository.findById(responseId);
    }
}
