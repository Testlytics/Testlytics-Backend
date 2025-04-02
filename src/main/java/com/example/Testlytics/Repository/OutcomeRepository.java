package com.example.Testlytics.Repository;

import com.example.Testlytics.Entity.Outcome;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface OutcomeRepository extends JpaRepository<Outcome, UUID> {
    List<Outcome> findByTestId(UUID testId);

    // ✅ Count correct outcomes for a specific test and user
    long countByTestIdAndUserIdAndIsCorrectTrue(UUID testId, Integer userId);
    List<Outcome> findByTestIdAndUserId(UUID testId, Integer userId);
    
    Optional<Outcome> findByTestIdAndUserIdAndQuestionId(UUID testId, Integer userId, UUID questionId);
    // In OutcomeRepository interface
long countByTestIdAndUserIdAndIsCorrect(UUID testId, Integer userId, boolean isCorrect);
long countByTestIdAndUserId(UUID testId, Integer userId);


}
