package com.example.Testlytics.Service;

import com.example.Testlytics.DTO.OptionsDTO;
import com.example.Testlytics.Entity.Options;
import com.example.Testlytics.Entity.Question;
import com.example.Testlytics.Repository.OptionsRepository;
import com.example.Testlytics.Repository.QuestionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class OptionsService {

    @Autowired
    private OptionsRepository optionsRepository;

    @Autowired
    private QuestionRepository questionRepository;

    // Get all options for a specific question
    public List<OptionsDTO> getOptionsByQuestionId(UUID questionId) {
        return optionsRepository.findByQuestionQuestionId(questionId).stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    // Get a specific option by ID
    public OptionsDTO getOptionById(UUID id) {
        Options option = optionsRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Option not found with id: " + id));
        return convertToDTO(option);
    }

    // Create a new option for a question
    public OptionsDTO createOption(OptionsDTO optionDTO) {
        Question question = questionRepository.findById(optionDTO.getQuestionId())
                .orElseThrow(() -> new RuntimeException("Question not found with id: " + optionDTO.getQuestionId()));

        Options option = new Options();
        option.setOptionText(optionDTO.getOptionText());
        option.setCorrect(optionDTO.isCorrect());
        option.setQuestion(question);

        return convertToDTO(optionsRepository.save(option));
    }

    // Update an existing option
    public OptionsDTO updateOption(UUID optionId, OptionsDTO updatedOptionDTO) {
        Options existingOption = optionsRepository.findById(optionId)
                .orElseThrow(() -> new RuntimeException("Option not found with id: " + optionId));

        existingOption.setOptionText(updatedOptionDTO.getOptionText());
        existingOption.setCorrect(updatedOptionDTO.isCorrect());

        return convertToDTO(optionsRepository.save(existingOption));
    }

    // Delete an option completely
    public void deleteOption(UUID optionId) {
        if (!optionsRepository.existsById(optionId)) {
            throw new RuntimeException("Option not found with id: " + optionId);
        }
        optionsRepository.deleteById(optionId);
    }

    // Convert entity to DTO
    private OptionsDTO convertToDTO(Options option) {
        return new OptionsDTO(
                option.getOptionId(),
                option.getQuestion().getQuestionId(),
                option.getOptionText(),
                option.isCorrect()
        );
    }
}
