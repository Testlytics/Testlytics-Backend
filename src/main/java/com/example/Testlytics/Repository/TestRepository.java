package com.example.Testlytics.Repository;

import com.example.Testlytics.Entity.Test;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface TestRepository extends JpaRepository<Test, UUID> {

    @Query("SELECT t FROM Test t WHERE t.deletedOn IS NULL")
    List<Test> findAllActiveTests();

    @Query("SELECT t FROM Test t WHERE t.testId = :id AND t.deletedOn IS NULL")
    Optional<Test> findActiveTestById(UUID id);

    @Query("SELECT COUNT(t) > 0 FROM Test t WHERE t.deletedOn IS NULL AND t.testDate = :testDate " +
           "AND ((:startTime BETWEEN t.startTime AND t.endTime) OR (:endTime BETWEEN t.startTime AND t.endTime) " +
           "OR (t.startTime BETWEEN :startTime AND :endTime) OR (t.endTime BETWEEN :startTime AND :endTime))")
    boolean existsConflictingTest(LocalDate testDate, LocalTime startTime, LocalTime endTime);
    @Query("SELECT t FROM Test t WHERE t.deletedOn IS NULL AND t.testDate > CURRENT_DATE " +
       "OR (t.testDate = CURRENT_DATE AND t.endTime > CURRENT_TIME)")
List<Test> findUpcomingTests();

@Query("SELECT t FROM Test t WHERE t.deletedOn IS NULL AND " +
       "(t.testDate < CURRENT_DATE OR (t.testDate = CURRENT_DATE AND t.endTime <= CURRENT_TIME))")
List<Test> findCompletedTests();


}
