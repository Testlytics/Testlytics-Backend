package com.example.Testlytics.Repository;

import com.example.Testlytics.Entity.Response;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.UUID;

@Repository
public interface ResponseRepository extends JpaRepository<Response, UUID> {
    List<Response> findByTestTestId(UUID testId);
}
