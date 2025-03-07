package com.example.Testlytics.Service;

import com.example.Testlytics.DTO.QuestionDTO;
import com.example.Testlytics.Entity.Question;
import com.example.Testlytics.Entity.TestDetails;
import com.example.Testlytics.Repository.QuestionRepository;
import com.example.Testlytics.Repository.TestDetailsRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class QuestionService {

    private final QuestionRepository questionRepository;
    private final TestDetailsRepository testDetailsRepository;

    public QuestionDTO createQuestion(UUID testId, String questionText, String answer) {
        // Fetch TestDetails by testId
        TestDetails testDetails = testDetailsRepository.findById(testId)
                .orElseThrow(() -> new RuntimeException("TestDetails not found for ID: " + testId));

        // Create and save Question
        Question question = new Question();
        question.setTestDetails(testDetails); // ✅ Correctly set TestDetails entity
        question.setQuestionText(questionText);
        question.setAnswer(answer);

        Question savedQuestion = questionRepository.save(question);

        return new QuestionDTO(
                savedQuestion.getQuestionId(),
                savedQuestion.getTestDetails().getTestId(), // ✅ Get testId from TestDetails
                savedQuestion.getQuestionText(),
                savedQuestion.getAnswer()
        );
    }

    public List<QuestionDTO> getAllQuestions() {
        List<Question> questions = questionRepository.findAll();
        return questions.stream().map(question -> new QuestionDTO(
                question.getQuestionId(),
                question.getTestDetails().getTestId(), // ✅ Get testId from TestDetails
                question.getQuestionText(),
                question.getAnswer()
        )).collect(Collectors.toList());
    }

    public QuestionDTO getQuestionById(UUID questionId) {
        Question question = questionRepository.findById(questionId)
                .orElseThrow(() -> new RuntimeException("Question not found for ID: " + questionId));

        return new QuestionDTO(
                question.getQuestionId(),
                question.getTestDetails().getTestId(), // ✅ Get testId from TestDetails
                question.getQuestionText(),
                question.getAnswer()
        );
    }
}
