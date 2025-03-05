package com.example.Testlytics.Service;

import com.example.Testlytics.DTO.QuestionDTO;
import com.example.Testlytics.Entity.Question;
import com.example.Testlytics.Entity.TestDetails;
import com.example.Testlytics.Repository.QuestionRepository;
import com.example.Testlytics.Repository.TestDetailsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.UUID;

@Service
public class QuestionService {

    @Autowired
    private QuestionRepository questionRepository;

    @Autowired
    private TestDetailsRepository testDetailsRepository; // Fetch test entity

    // Create a new question
    public QuestionDTO createQuestion(UUID testId, String questionText, MultipartFile image, UUID correctOptionId) {
        // Fetch TestDetails entity
        TestDetails test = testDetailsRepository.findById(testId)
                .orElseThrow(() -> new RuntimeException("Test not found"));

        // Create Question entity
        Question question = new Question();
        question.setTest(test);
        question.setQuestionText(questionText);
        question.setCorrectOptionId(correctOptionId);

        // Handle image if provided
        if (image != null && !image.isEmpty()) {
            try {
                question.setImage(image.getBytes());
            } catch (Exception e) {
                throw new RuntimeException("Failed to upload image");
            }
        }

        Question savedQuestion = questionRepository.save(question);
        return new QuestionDTO(
                savedQuestion.getQuestionId(),
                savedQuestion.getTest().getTestId(), // Extract only testId
                savedQuestion.getQuestionText(),
                savedQuestion.getImage(),
                savedQuestion.getCorrectOptionId()
        );
    }

    // Get all questions for a test
    public List<QuestionDTO> getQuestionsByTestId(UUID testId) {
        return questionRepository.findByTest_TestId(testId)
                .stream()
                .map(q -> new QuestionDTO(
                        q.getQuestionId(),
                        q.getTest().getTestId(), // Extract only testId
                        q.getQuestionText(),
                        q.getImage(),
                        q.getCorrectOptionId()
                ))
                .toList();
    }

    // Get a specific question by ID
    public QuestionDTO getQuestionById(UUID testId, UUID questionId) {
        Question question = questionRepository.findByTest_TestIdAndQuestionId(testId, questionId)
                .orElseThrow(() -> new RuntimeException("Question not found"));
        return new QuestionDTO(
                question.getQuestionId(),
                question.getTest().getTestId(), // Extract only testId
                question.getQuestionText(),
                question.getImage(),
                question.getCorrectOptionId()
        );
    }

    // Update a question
    public QuestionDTO updateQuestion(UUID testId, UUID questionId, String updatedQuestionText, MultipartFile updatedImage, UUID updatedCorrectOptionId) {
        Question existingQuestion = questionRepository.findByTest_TestIdAndQuestionId(testId, questionId)
                .orElseThrow(() -> new RuntimeException("Question not found"));
if(updatedImage != null && !updatedImage.isEmpty()) {
            try {
                existingQuestion.setImage(updatedImage.getBytes());
            } catch (Exception e) {
                throw new RuntimeException("Failed to upload new image");
            }
        }

        Question updatedQuestion = questionRepository.save(existingQuestion);
        return new QuestionDTO(
                updatedQuestion.getQuestionId(),
                updatedQuestion.getTest().getTestId(), // Extract only testId
                updatedQuestion.getQuestionText(),
                updatedQuestion.getImage(),
                updatedQuestion.getCorrectOptionId()
        );
    }

    // Delete a question
    public void deleteQuestion(UUID testId, UUID questionId) {
        Question question = questionRepository.findByTest_TestIdAndQuestionId(testId, questionId)
                .orElseThrow(() -> new RuntimeException("Question not found"));
        questionRepository.delete(question);
    }
}
