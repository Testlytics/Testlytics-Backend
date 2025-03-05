package com.example.Testlytics.Repository;

import com.example.Testlytics.Entity.Question;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface QuestionRepository extends JpaRepository<Question, UUID> {

    // Fetch all questions related to a specific test
    List<Question> findByTest_TestId(UUID testId); // Fix method name

    // Fetch a specific question by testId and questionId
    Optional<Question> findByTest_TestIdAndQuestionId(UUID testId, UUID questionId); // Fix method name
}
