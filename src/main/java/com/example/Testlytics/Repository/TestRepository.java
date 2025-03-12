package com.example.Testlytics.Repository;

import com.example.Testlytics.Entity.Test;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface TestRepository extends JpaRepository<Test, UUID> {

    @Query("SELECT t FROM Test t WHERE t.deletedOn IS NULL")
    List<Test> findAllActiveTests();

    @Query("SELECT t FROM Test t WHERE t.testId = :id AND t.deletedOn IS NULL")
    Optional<Test> findActiveTestById(UUID id);
}
