package com.example.Testlytics.Service;

import com.example.Testlytics.DTO.OutcomeDTO;
import com.example.Testlytics.Entity.Options;
import com.example.Testlytics.Entity.Outcome;
import com.example.Testlytics.Repository.OptionsRepository;
import com.example.Testlytics.Repository.OutcomeRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class OutcomeService {

    private final OutcomeRepository outcomeRepository;
    private final OptionsRepository optionsRepository;

    public OutcomeService(OutcomeRepository outcomeRepository, OptionsRepository optionsRepository) {
        this.outcomeRepository = outcomeRepository;
        this.optionsRepository = optionsRepository;
    }

    public Outcome submitOutcome(UUID testId, OutcomeDTO.SubmitOutcome request) {
        // Fetch the selected option from the database
        Options selectedOption = optionsRepository.findById(request.getSelectedOptionId())
                .orElseThrow(() -> new RuntimeException("Option not found with id: " + request.getSelectedOptionId()));
    
        // Check if an outcome already exists for the given testId, userId, and questionId
        Outcome outcome = outcomeRepository.findByTestIdAndUserIdAndQuestionId(testId, request.getUserId(), request.getQuestionId())
                .orElse(new Outcome()); // If not found, create a new one
    
        // Set/update fields
        outcome.setTestId(testId);
        outcome.setQuestionId(request.getQuestionId());
        outcome.setUserId(request.getUserId());
        outcome.setSelectedOptionId(request.getSelectedOptionId());
        outcome.setIsCorrect(selectedOption.isCorrect());
    
        // Save outcome (update if exists, insert if new)
        return outcomeRepository.save(outcome);
    }
    

  
    public List<Outcome> getUserOutcomesByTestId(UUID testId, Integer userId) {
        return outcomeRepository.findByTestIdAndUserId(testId, userId);
    }
    
}
