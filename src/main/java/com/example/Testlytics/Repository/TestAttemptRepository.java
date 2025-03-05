package com.example.Testlytics.Repository;

import com.example.Testlytics.Entity.TestAttempt;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface TestAttemptRepository extends JpaRepository<TestAttempt, Long> {
    List<TestAttempt> findByUserId(Long userId);
    List<TestAttempt> findByTestId(Long testId);
}
