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
    public double calculateAccuracy(UUID testId, Integer userId) {
        List<Outcome> outcomes = outcomeRepository.findByTestIdAndUserId(testId, userId);
    
        // If no outcomes exist, return 0%
        if (outcomes == null || outcomes.isEmpty()) {
            return 0.0;
        }
    
        // Count only answered questions (selectedOptionId != null)
        long totalAnswered = outcomes.stream()
            .filter(o -> o.getSelectedOptionId() != null)
            .count();
    
        // If no questions were answered, return 0%
        if (totalAnswered == 0) {
            return 0.0;
        }
    
        // Count correct answers
        long correctAnswers = outcomes.stream()
            .filter(o -> o.getSelectedOptionId() != null && Boolean.TRUE.equals(o.getIsCorrect()))
            .count();
    
        // Calculate accuracy (rounded to 2 decimal places)
        return Math.round((correctAnswers * 100.0 / totalAnswered) * 100) / 100.0;
    }
}
