package com.example.Testlytics.Repository;

import com.example.Testlytics.Entity.Options;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface OptionsRepository extends JpaRepository<Options, UUID> {

    List<Options> findByQuestion_QuestionId(UUID questionId);
}
