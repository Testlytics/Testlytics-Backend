package com.example.Testlytics.Repository;

import com.example.Testlytics.Entity.Question;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import java.util.*;

@Repository
public interface QuestionRepository extends JpaRepository<Question, UUID> {

    List<Question> findByTest_TestId(UUID testId);

    @Query("SELECT q FROM Question q WHERE q.questionId = :questionId")
    Optional<Question> findByIdWithImage(UUID questionId);
}
