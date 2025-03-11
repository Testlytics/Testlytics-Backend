package com.example.Testlytics.Repository;

import com.example.Testlytics.Entity.TestDetails;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface TestDetailsRepository extends JpaRepository<TestDetails, UUID> {

    @Query("SELECT t FROM TestDetails t WHERE t.deletedOn IS NULL")
    List<TestDetails> findAllActiveTests();

    @Query("SELECT t FROM TestDetails t WHERE t.testId = :id AND t.deletedOn IS NULL")
    Optional<TestDetails> findActiveTestById(UUID id);

}
