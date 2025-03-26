package com.example.Testlytics.Repository;

import com.example.Testlytics.Entity.TestAttempt;
import com.example.Testlytics.Entity.TestAttemptId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface TestAttemptRepository extends JpaRepository<TestAttempt, TestAttemptId> {

    // ✅ Get all test attempts of a user
    @Query("SELECT ta FROM TestAttempt ta WHERE ta.id.userId = :userId")
    List<TestAttempt> findTestAttemptsByUserId(@Param("userId") Integer userId);

    // ✅ Get all users who attempted a particular test
    @Query("SELECT ta.id.userId FROM TestAttempt ta WHERE ta.id.testId = :testId")
    List<Integer> findUsersByTestId(@Param("testId") UUID testId);

    // ✅ Get average score of a subject (by joining with Test entity)
    @Query("SELECT AVG(ta.score) FROM TestAttempt ta " +
           "JOIN Test t ON ta.id.testId = t.testId " +
           "WHERE t.subjectId = :subjectId")
    Optional<Double> findAverageScoreBySubject(@Param("subjectId") UUID subjectId);
}
