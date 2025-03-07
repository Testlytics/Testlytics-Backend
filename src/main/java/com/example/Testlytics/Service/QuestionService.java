package com.example.Testlytics.Service;

import com.example.Testlytics.DTO.QuestionDTO;
import com.example.Testlytics.Entity.Question;
import com.example.Testlytics.Entity.TestDetails;
import com.example.Testlytics.Repository.QuestionRepository;
import com.example.Testlytics.Repository.TestDetailsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Base64;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class QuestionService {

    @Autowired
    private QuestionRepository questionRepository;

    @Autowired
    private TestDetailsRepository testDetailsRepository; // Fetch test entity

    // Create a new question (Without Image)
    public QuestionDTO createQuestion(UUID testId, String questionText, MultipartFile image, String answer) {
        // Fetch TestDetails entity
        TestDetails test = testDetailsRepository.findById(testId)
                .orElseThrow(() -> new RuntimeException("Test not found"));

        // Create Question entity
        Question question = new Question();
        question.setTest(test);
        question.setQuestionText(questionText);
        question.setAnswer(answer); // Set answer

        Question savedQuestion = questionRepository.save(question);
        return new QuestionDTO(
                savedQuestion.getQuestionId(),
                savedQuestion.getTest().getTestId(), // Extract only testId
                savedQuestion.getQuestionText(),
                null, // No image in response
                savedQuestion.getAnswer()
        );
    }

    // Upload Image for a Question
    public void uploadImage(UUID questionId, MultipartFile file) {
        Optional<Question> questionOptional = questionRepository.findById(questionId);
        if (questionOptional.isPresent()) {
            Question question = questionOptional.get();
            try {
                question.setImage(file.getBytes()); // Convert MultipartFile to byte[]
                questionRepository.save(question);
            } catch (IOException e) {
                throw new RuntimeException("Failed to upload image");
            }
        } else {
            throw new RuntimeException("Question not found!");
        }
    }

    // Update Existing Image for a Question
    public void updateImage(UUID questionId, MultipartFile file) {
        Optional<Question> questionOptional = questionRepository.findById(questionId);
        if (questionOptional.isPresent()) {
            Question question = questionOptional.get();
            try {
                question.setImage(file.getBytes()); // Convert MultipartFile to byte[]
                questionRepository.save(question);
            } catch (IOException e) {
                throw new RuntimeException("Failed to update image");
            }
        } else {
            throw new RuntimeException("Question not found!");
        }
    }

    // Retrieve Image Data for a Question
    public String getImageByQuestionId(UUID questionId) {
        Optional<Question> questionOptional = questionRepository.findById(questionId);
        if (questionOptional.isPresent() && questionOptional.get().getImage() != null) {
            byte[] imageBytes = questionOptional.get().getImage();
            return Base64.getEncoder().encodeToString(imageBytes);
        }
        throw new RuntimeException("Question image not found!");
    }

    // Get all questions for a test
    public List<QuestionDTO> getQuestionsByTestId(UUID testId) {
        return questionRepository.findByTest_TestId(testId)
                .stream()
                .map(q -> new QuestionDTO(
                        q.getQuestionId(),
                        q.getTest().getTestId(),
                        q.getQuestionText(),
                        q.getImage(),
                        q.getAnswer()
                ))
                .toList();
    }

    // Get a specific question by ID
    public QuestionDTO getQuestionById(UUID testId, UUID questionId) {
        Question question = questionRepository.findByTest_TestIdAndQuestionId(testId, questionId)
                .orElseThrow(() -> new RuntimeException("Question not found"));
        return new QuestionDTO(
                question.getQuestionId(),
                question.getTest().getTestId(),
                question.getQuestionText(),
                question.getImage(),
                question.getAnswer()
        );
    }

    // Update a question (Without Image)
    public QuestionDTO updateQuestion(UUID testId, UUID questionId, String updatedQuestionText, MultipartFile updatedImage, String updatedAnswer) {
        Question existingQuestion = questionRepository.findByTest_TestIdAndQuestionId(testId, questionId)
                .orElseThrow(() -> new RuntimeException("Question not found"));

        existingQuestion.setQuestionText(updatedQuestionText);
        existingQuestion.setAnswer(updatedAnswer);

        Question updatedQuestion = questionRepository.save(existingQuestion);
        return new QuestionDTO(
                updatedQuestion.getQuestionId(),
                updatedQuestion.getTest().getTestId(),
                updatedQuestion.getQuestionText(),
                null, // No image in response
                updatedQuestion.getAnswer()
        );
    }

    // Delete a question
    public void deleteQuestion(UUID testId, UUID questionId) {
        Question question = questionRepository.findByTest_TestIdAndQuestionId(testId, questionId)
                .orElseThrow(() -> new RuntimeException("Question not found"));
        questionRepository.delete(question);
    }
}
