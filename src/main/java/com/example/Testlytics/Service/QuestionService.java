package com.example.Testlytics.Service;

import com.example.Testlytics.DTO.OptionDTO;
import com.example.Testlytics.DTO.QuestionDTO;
import com.example.Testlytics.Entity.Options;
import com.example.Testlytics.Entity.Question;
import com.example.Testlytics.Entity.Test;
import com.example.Testlytics.Repository.OptionsRepository;
import com.example.Testlytics.Repository.QuestionRepository;
import com.example.Testlytics.Repository.TestRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class QuestionService {

    @Autowired
    private QuestionRepository questionRepository;

    @Autowired
    private TestRepository testRepository;

    @Autowired
    private OptionsRepository optionsRepository;

    public QuestionDTO createQuestion(UUID testId, String questionText, String answer, List<OptionDTO> options) {
        Test test = testRepository.findById(testId)
                .orElseThrow(() -> new RuntimeException("Test not found for ID: " + testId));
    
        Question question = new Question();
        question.setTest(test);
        question.setQuestionText(questionText);
        question.setAnswer(answer);
    
        // Save the question first
        Question savedQuestion = questionRepository.save(question);
    
        // Save options and automatically set the correct option based on answer
        List<Options> savedOptions = options.stream().map(opt -> {
            Options option = new Options();
            option.setQuestion(savedQuestion);
            option.setOptionText(opt.getOptionText());
            option.setCorrect(opt.getOptionText().equalsIgnoreCase(answer)); // ✅ Automatically mark correct option
            return optionsRepository.save(option);
        }).collect(Collectors.toList());
    
        return new QuestionDTO(
                savedQuestion.getQuestionId(), 
                testId, 
                savedQuestion.getQuestionText(), 
                savedQuestion.getAnswer(),
                savedOptions.stream().map(this::convertToDTO).collect(Collectors.toList())
        );
    }
    
    

    public QuestionDTO getQuestionById(UUID questionId) {
        Question question = questionRepository.findById(questionId)
                .orElseThrow(() -> new RuntimeException("Question not found"));

        List<OptionDTO> options = optionsRepository.findByQuestion_QuestionId(questionId)
                .stream().map(this::convertToDTO).collect(Collectors.toList());

        return new QuestionDTO(question.getQuestionId(), question.getTest().getTestId(), question.getQuestionText(), question.getAnswer(), options);
    }

    public void deleteQuestion(UUID questionId) {
        Question question = questionRepository.findById(questionId)
                .orElseThrow(() -> new RuntimeException("Question not found"));
        optionsRepository.deleteAll(optionsRepository.findByQuestion_QuestionId(questionId));
        questionRepository.delete(question);
    }

    private OptionDTO convertToDTO(Options option) {
        return new OptionDTO(option.getOptionId(), option.getOptionText(), option.isCorrect());
    }
}
