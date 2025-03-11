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


    public OptionsDTO createOption(UUID questionId, String optionText, boolean isCorrect) {
        Question question = questionRepository.findById(questionId)
                .orElseThrow(() -> new RuntimeException("Question not found with id: " + questionId));

        Options option = Options.builder()
                .optionText(optionText)
                .isCorrect(isCorrect)
                .question(question)
                .build();

        return convertToDTO(optionsRepository.save(option));
    }


    public List<OptionsDTO> getOptionsByQuestionId(UUID questionId) {


        List<Options> options = optionsRepository.findByQuestion_QuestionId(questionId);

        return options.stream()
                .map(option -> new OptionsDTO(
                        option.getOptionId(),
                        option.getQuestion().getQuestionId(), // Ensure `getQuestion()` returns a `Question` entity
                        option.getOptionText(),
                        option.isCorrect()
                ))
                .collect(Collectors.toList());

    }

    public OptionsDTO updateOption(UUID optionId, String optionText, boolean isCorrect) {
        Options option = optionsRepository.findById(optionId)
                .orElseThrow(() -> new RuntimeException("Option not found with id: " + optionId));

        option.setOptionText(optionText);
        option.setCorrect(isCorrect);

        Options updatedOption = optionsRepository.save(option);
        return new OptionsDTO(updatedOption.getOptionId(), updatedOption.getQuestion().getQuestionId(), updatedOption.getOptionText(), updatedOption.isCorrect());
    }

    public OptionsDTO getOptionById(UUID optionId) {
        Options option = optionsRepository.findById(optionId)
                .orElseThrow(() -> new RuntimeException("Option not found with id: " + optionId));

        return new OptionsDTO(option.getOptionId(), option.getQuestion().getQuestionId(), option.getOptionText(), option.isCorrect());
    }


    public void deleteOption(UUID optionId) {
        Options option = optionsRepository.findById(optionId)
                .orElseThrow(() -> new RuntimeException("Option not found with id: " + optionId));

        optionsRepository.delete(option);
    }


    // ✅ Convert entity to DTO
    private OptionsDTO convertToDTO(Options option) {
        return new OptionsDTO(
                option.getOptionId(),
                option.getQuestion().getQuestionId(),
                option.getOptionText(),
                option.isCorrect()
        );
    }
}
