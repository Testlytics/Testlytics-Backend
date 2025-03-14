package com.example.Testlytics.Service;

import com.example.Testlytics.DTO.QuestionDTO;
import com.example.Testlytics.Entity.Question;
import com.example.Testlytics.Entity.Test;
import com.example.Testlytics.Repository.QuestionRepository;
import com.example.Testlytics.Repository.TestRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class QuestionService {

    @Autowired
    private QuestionRepository questionRepository;

    @Autowired
    private TestRepository testRepository;

    public QuestionDTO createQuestion(UUID testId, String questionText, String answer) {
        Test test = testRepository.findById(testId)
                .orElseThrow(() -> new RuntimeException("Test not found for ID: " + testId));

        Question question = new Question();
        question.setTest(test);
        question.setQuestionText(questionText);
        question.setAnswer(answer);

        Question savedQuestion = questionRepository.save(question);
        return new QuestionDTO(savedQuestion.getQuestionId(), testId, savedQuestion.getQuestionText(), savedQuestion.getAnswer());
    }

    public QuestionDTO getQuestionById(UUID questionId) {
        Question question = questionRepository.findById(questionId)
                .orElseThrow(() -> new RuntimeException("Question not found"));

        return new QuestionDTO(question.getQuestionId(), question.getTest().getTestId(), question.getQuestionText(), question.getAnswer());
    }

    public List<QuestionDTO> getQuestionsByTestId(UUID testId) {
        List<Question> questions = questionRepository.findByTest_TestId(testId);
        return questions.stream()
                .map(q -> new QuestionDTO(q.getQuestionId(), q.getTest().getTestId(), q.getQuestionText(), q.getAnswer()))
                .collect(Collectors.toList());
    }

    public QuestionDTO updateQuestion(UUID questionId, String questionText, String answer) {
        Question question = questionRepository.findById(questionId)
                .orElseThrow(() -> new RuntimeException("Question not found"));

        question.setQuestionText(questionText);
        question.setAnswer(answer);
        Question updatedQuestion = questionRepository.save(question);

        return new QuestionDTO(updatedQuestion.getQuestionId(), updatedQuestion.getTest().getTestId(), updatedQuestion.getQuestionText(), updatedQuestion.getAnswer());
    }

    public void deleteQuestion(UUID questionId) {
        Question question = questionRepository.findById(questionId)
                .orElseThrow(() -> new RuntimeException("Question not found"));
        questionRepository.delete(question);
    }

    public String uploadQuestionImage(UUID questionId, MultipartFile imageFile) {
        Question question = questionRepository.findById(questionId)
                .orElseThrow(() -> new RuntimeException("Question not found"));

        try {
            question.setImage(imageFile.getBytes());
            questionRepository.save(question);
            return "Image uploaded successfully";
        } catch (IOException e) {
            throw new RuntimeException("Failed to store image", e);
        }
    }

    public byte[] getQuestionImage(UUID questionId) {
        Question question = questionRepository.findById(questionId)
                .orElseThrow(() -> new RuntimeException("Question not found"));

        if (question.getImage() == null) {
            throw new RuntimeException("No image found for this question");
        }
        return (byte[]) question.getImage();
    }

    public String updateQuestionImage(UUID questionId, MultipartFile imageFile) {
        Question question = questionRepository.findById(questionId)
                .orElseThrow(() -> new RuntimeException("Question not found"));

        try {
            question.setImage(imageFile.getBytes());
            questionRepository.save(question);
            return "Image updated successfully";
        } catch (IOException e) {
            return "Error updating image: " + e.getMessage();
        }
    }
}
