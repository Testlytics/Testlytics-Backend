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

    // ✅ Create a new question
    public QuestionDTO createQuestion(UUID testId, String questionText, String answer) {
        TestDetails testDetails = testDetailsRepository.findById(testId)
                .orElseThrow(() -> new RuntimeException("TestDetails not found for ID: " + testId));

        Question question = new Question();
        question.setTestDetails(testDetails);
        question.setQuestionText(questionText);
        question.setAnswer(answer);

        Question savedQuestion = questionRepository.save(question);

        return new QuestionDTO(
                savedQuestion.getQuestionId(),
                savedQuestion.getTestDetails().getTestId(),
                savedQuestion.getQuestionText(),
                savedQuestion.getAnswer()
        );
    }

    // ✅ Get all questions
    public List<QuestionDTO> getAllQuestions() {
        List<Question> questions = questionRepository.findAll();
        return questions.stream()
                .map(q -> new QuestionDTO(
                        q.getQuestionId(),
                        q.getTestDetails().getTestId(),
                        q.getQuestionText(),
                        q.getAnswer()))
                .collect(Collectors.toList());
    }

    // ✅ Get a question by its ID
    public QuestionDTO getQuestionById(UUID questionId) {
        Question question = questionRepository.findById(questionId)
                .orElseThrow(() -> new RuntimeException("Question not found for ID: " + questionId));

        return new QuestionDTO(
                question.getQuestionId(),
                question.getTestDetails().getTestId(),
                question.getQuestionText(),
                question.getAnswer()
        );
    }

    // ✅ Get all questions by test ID
    public List<QuestionDTO> getQuestionsByTestId(UUID testId) {
        List<Question> questions = questionRepository.findByTestDetails_TestId(testId);
        return questions.stream()
                .map(q -> new QuestionDTO(
                        q.getQuestionId(),
                        q.getTestDetails().getTestId(),
                        q.getQuestionText(),
                        q.getAnswer()))
                .collect(Collectors.toList());
    }
}
