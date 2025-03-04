package com.example.Testlytics.Repository;

import com.example.Testlytics.Entity.TestAttempt;
import com.example.Testlytics.Entity.TestAttemptId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface TestAttemptRepository extends JpaRepository<TestAttempt, Long> {
    Optional<TestAttempt> findById(TestAttemptId attemptId);
}