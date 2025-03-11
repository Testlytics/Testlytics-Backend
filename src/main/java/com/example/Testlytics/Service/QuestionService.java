package com.example.Testlytics.Service;

import com.example.Testlytics.DTO.QuestionDTO;
import com.example.Testlytics.Entity.Question;
import com.example.Testlytics.Entity.TestDetails;
import com.example.Testlytics.Repository.QuestionRepository;
import com.example.Testlytics.Repository.TestDetailsRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service

public class QuestionService {

    @Autowired
    private QuestionRepository questionRepository;

    @Autowired
    private TestDetailsRepository testDetailsRepository;

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

    public List<QuestionDTO> getQuestionsByTestId(UUID testId) {
        try {
            List<Question> questions = questionRepository.findByTestDetails_TestId(testId);

            if (questions.isEmpty()) {
                throw new RuntimeException("No questions found for test ID: " + testId);
            }

            return questions.stream()
                    .map(q -> new QuestionDTO(
                            q.getQuestionId(),
                            q.getTestDetails().getTestId(),
                            q.getQuestionText(),
                            q.getAnswer()))
                    .collect(Collectors.toList());
        } catch (Exception e) {
            throw new RuntimeException("Error fetching questions for test ID: " + testId, e);
        }
    }



    // ✅ Update a question for a specific test
    public QuestionDTO updateQuestion(UUID testId, UUID questionId, String questionText, String answer) {
        // Validate if the test exists
        TestDetails testDetails = testDetailsRepository.findById(testId)
                .orElseThrow(() -> new RuntimeException("TestDetails not found for ID: " + testId));

        // Find the question
        Question question = questionRepository.findById(questionId)
                .orElseThrow(() -> new RuntimeException("Question not found for ID: " + questionId));

        // Ensure the question belongs to the given test
        if (!question.getTestDetails().getTestId().equals(testId)) {
            throw new RuntimeException("Question does not belong to the given test ID: " + testId);
        }

        // Update question details
        question.setQuestionText(questionText);
        question.setAnswer(answer);
        Question updatedQuestion = questionRepository.save(question);

        return new QuestionDTO(
                updatedQuestion.getQuestionId(),
                updatedQuestion.getTestDetails().getTestId(),
                updatedQuestion.getQuestionText(),
                updatedQuestion.getAnswer()
        );
    }

    public void deleteQuestion(UUID testId, UUID questionId) {
        // Validate if the test exists
        TestDetails testDetails = testDetailsRepository.findById(testId)
                .orElseThrow(() -> new RuntimeException("TestDetails not found for ID: " + testId));

        // Find the question
        Question question = questionRepository.findById(questionId)
                .orElseThrow(() -> new RuntimeException("Question not found for ID: " + questionId));

        // Ensure the question belongs to the given test
        if (!question.getTestDetails().getTestId().equals(testId)) {
            throw new RuntimeException("Question does not belong to the given test ID: " + testId);
        }

        // Delete the question
        questionRepository.delete(question);
    }



}
