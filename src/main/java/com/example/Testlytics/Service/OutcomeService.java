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

    public Outcome submitOutcome(OutcomeDTO.SubmitOutcome request) {
        // Fetch the selected option from the database
        Options selectedOption = optionsRepository.findById(request.getSelectedOptionId())
                .orElseThrow(() -> new RuntimeException("Option not found with id: " + request.getSelectedOptionId()));

        // Create a new Outcome entity
        Outcome outcome = new Outcome();
        outcome.setTestId(request.getTestId());
        outcome.setQuestionId(request.getQuestionId());
        outcome.setUserId(request.getUserId());
        outcome.setSelectedOptionId(request.getSelectedOptionId());

        // Set isCorrect based on the selected option's correctness
        outcome.setIsCorrect(selectedOption.isCorrect());

        // Save outcome
        return outcomeRepository.save(outcome);
    }

    public List<Outcome> getOutcomesByTestId(UUID testId) {
        return outcomeRepository.findByTestId(testId);
    }
}
