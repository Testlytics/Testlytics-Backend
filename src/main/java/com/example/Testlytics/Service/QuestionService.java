package com.example.Testlytics.Service;

import com.example.Testlytics.DTO.OptionDTO;
import com.example.Testlytics.DTO.QuestionDTO;
import com.example.Testlytics.Entity.Options;
import com.example.Testlytics.Entity.Question;
import com.example.Testlytics.Entity.Test;
import com.example.Testlytics.Repository.OptionsRepository;
import com.example.Testlytics.Repository.QuestionRepository;
import com.example.Testlytics.Repository.TestRepository;
import com.itextpdf.kernel.pdf.*;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.element.Text;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.ByteArrayOutputStream;
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

    /**
     * Creates a new question and stores it in the database.
     */
    public QuestionDTO createQuestion(UUID testId, String questionText, String answer, List<OptionDTO> options) {
        Test test = testRepository.findById(testId)
                .orElseThrow(() -> new RuntimeException("Test not found for ID: " + testId));

        Question question = new Question();
        question.setTest(test);
        question.setQuestionText(questionText);
        question.setAnswer(answer);

        Question savedQuestion = questionRepository.save(question);

        List<Options> savedOptions = options.stream().map(opt -> {
            Options option = new Options();
            option.setQuestion(savedQuestion);
            option.setOptionText(opt.getOptionText());
            option.setCorrect(opt.getOptionText().equalsIgnoreCase(answer)); // Automatically mark correct option
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

    /**
     * Fetches a question by its ID.
     */
    public QuestionDTO getQuestionById(UUID questionId) {
        Question question = questionRepository.findById(questionId)
                .orElseThrow(() -> new RuntimeException("Question not found"));

        List<OptionDTO> options = optionsRepository.findByQuestion_QuestionId(questionId)
                .stream().map(this::convertToDTO).collect(Collectors.toList());

        return new QuestionDTO(question.getQuestionId(), question.getTest().getTestId(),
                question.getQuestionText(), question.getAnswer(), options);
    }

    /**
     * Deletes a question and its associated options.
     */
    public void deleteQuestion(UUID questionId) {
        Question question = questionRepository.findById(questionId)
                .orElseThrow(() -> new RuntimeException("Question not found"));
        optionsRepository.deleteAll(optionsRepository.findByQuestion_QuestionId(questionId));
        questionRepository.delete(question);
    }

    /**
     * Converts an Options entity to an OptionDTO.
     */
    private OptionDTO convertToDTO(Options option) {
        return new OptionDTO(option.getOptionId(), option.getOptionText(), option.isCorrect());
    }

    /**
     * Updates a question and its options.
     */
    public QuestionDTO updateQuestion(UUID questionId, String questionText, String answer, List<OptionDTO> options) {
        Question question = questionRepository.findById(questionId)
                .orElseThrow(() -> new RuntimeException("Question not found"));

        question.setQuestionText(questionText);
        question.setAnswer(answer);
        Question updatedQuestion = questionRepository.save(question);

        optionsRepository.deleteAll(optionsRepository.findByQuestion_QuestionId(questionId));

        List<Options> savedOptions = options.stream().map(opt -> {
            Options option = new Options();
            option.setQuestion(updatedQuestion);
            option.setOptionText(opt.getOptionText());
            option.setCorrect(opt.getOptionText().equalsIgnoreCase(answer));
            return optionsRepository.save(option);
        }).collect(Collectors.toList());

        return new QuestionDTO(
                updatedQuestion.getQuestionId(),
                updatedQuestion.getTest().getTestId(),
                updatedQuestion.getQuestionText(),
                updatedQuestion.getAnswer(),
                savedOptions.stream().map(this::convertToDTO).collect(Collectors.toList())
        );
    }

    /**
     * Generates a PDF containing all questions and their options for a given test.
     */
    public byte[] generateQuestionsPdf(UUID testId) {
        List<Question> questions = questionRepository.findByTest_TestId(testId);

        if (questions.isEmpty()) {
            throw new RuntimeException("No questions found for this test.");
        }

        try (ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
             PdfWriter writer = new PdfWriter(outputStream);
             PdfDocument pdf = new PdfDocument(writer);
             Document document = new Document(pdf)) {

            // Title
            Text titleText = new Text("Test Questions").setFontSize(18);
            document.add(new Paragraph().add(titleText));

            for (Question question : questions) {
                // Add question
                document.add(new Paragraph("Q: " + question.getQuestionText()));

                // Fetch options
                List<Options> options = optionsRepository.findByQuestion_QuestionId(question.getQuestionId());
                for (Options option : options) {
                    String optionText = "- " + option.getOptionText() + (option.isCorrect() ? " (Correct)" : "");
                    document.add(new Paragraph(optionText));
                }
                document.add(new Paragraph("\n")); // Spacing
            }

            document.close();
            return outputStream.toByteArray();
        } catch (Exception e) {
            throw new RuntimeException("Error generating PDF", e);
        }
    }
}
