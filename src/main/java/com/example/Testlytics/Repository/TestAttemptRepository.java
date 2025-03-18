package com.example.Testlytics.Repository;

import com.example.Testlytics.Entity.TestAttempt;
import com.example.Testlytics.Entity.TestAttemptId;

import java.util.List;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface TestAttemptRepository extends JpaRepository<TestAttempt, TestAttemptId> {
     @Query("SELECT ta FROM TestAttempt ta WHERE ta.id.userId = :userId")
    List<TestAttempt> findTestAttemptsByUserId(Integer userId);

     @Query("SELECT ta.id.userId FROM TestAttempt ta WHERE ta.id.testId = :testId")
    List<Integer> findUsersByTestId(UUID testId);
}
