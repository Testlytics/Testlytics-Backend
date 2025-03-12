package com.example.Testlytics.Service;

import com.example.Testlytics.DTO.QuestionDTO;
import com.example.Testlytics.Entity.Question;
import com.example.Testlytics.Entity.TestDetails;
import com.example.Testlytics.Repository.QuestionRepository;
import com.example.Testlytics.Repository.TestDetailsRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.List;
import java.util.Optional;
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

    
    // ✅ Get a question by its ID
    public QuestionDTO getQuestionById(UUID questionId) {
        Question question = questionRepository.findById(questionId)
                .orElseThrow(() -> new RuntimeException("Question not found for ID: " + questionId));

        return new QuestionDTO(
                question.getQuestionId(),
                question.getTestDetails().getTestId(),
                question.getQuestionText(),
                question.getAnswer(),
                question.getImage()
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
                            q.getAnswer(),
                            q.getImage()))
                    .collect(Collectors.toList());
        } catch (Exception e) {
            throw new RuntimeException("Error fetching questions for test ID: " + testId, e);
        }
    }



    // ✅ Update a question for a specific test
    public QuestionDTO updateQuestion( UUID questionId, String questionText, String answer) {
       
      
        // Find the question
        Question question = questionRepository.findById(questionId)
                .orElseThrow(() -> new RuntimeException("Question not found for ID: " + questionId));

    

        // Update question details
        question.setQuestionText(questionText);
        question.setAnswer(answer);
        Question updatedQuestion = questionRepository.save(question);

        return new QuestionDTO(
                updatedQuestion.getQuestionId(),
                updatedQuestion.getTestDetails().getTestId(),
                updatedQuestion.getQuestionText(),
                updatedQuestion.getAnswer(),
                updatedQuestion.getImage()
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

    public String uploadQuestionImage(UUID questionId, MultipartFile imageFile) {
        // Validate if question exists
        Question question = questionRepository.findById(questionId)
                .orElseThrow(() -> new RuntimeException("Question not found"));

        try {
            // Convert file to byte array
            byte[] imageBytes = imageFile.getBytes();
            question.setImage(imageBytes);

            // Save to database
            questionRepository.save(question);
            return "Image uploaded successfully for question ID: " + questionId;
        } catch (IOException e) {
            throw new RuntimeException("Failed to store image", e);
        }
    }public byte[] getQuestionImage(UUID questionId) {
        Question question = questionRepository.findById(questionId)
                .orElseThrow(() -> new RuntimeException("Question not found"));

        byte[] image = question.getImage();
        if (image == null || image.length == 0) {
            throw new RuntimeException("No image found for this question");
        }

        return image;
    }

    public String updateQuestionImage(UUID questionId, MultipartFile imageFile) {
        Optional<Question> optionalQuestion = questionRepository.findByIdWithImage(questionId);

        if (optionalQuestion.isEmpty()) {
            return "Question not found, unable to update image.";
        }

        Question question = optionalQuestion.get();
        try {
            question.setImage(imageFile.getBytes());  // Convert MultipartFile to byte[]
            questionRepository.save(question);
            return "Image updated successfully";
        } catch (IOException e) {
            return "Error updating image: " + e.getMessage();
        }
    }



}
