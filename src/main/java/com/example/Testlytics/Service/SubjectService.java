package com.example.Testlytics.Service;

import com.example.Testlytics.Entity.Subject;
import com.example.Testlytics.Repository.SubjectRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class SubjectService {

    @Autowired
    private SubjectRepository subjectRepository;

    // Create a new subject
    public Subject createSubject(Subject subject) {
        return subjectRepository.save(subject);
    }

    // Retrieve all subjects
    public List<Subject> getAllSubjects() {
        return subjectRepository.findAll();
    }

    // Retrieve a specific subject by subjectId
    public Subject getSubjectById(UUID subjectId) {
        return subjectRepository.findById(subjectId)
                .orElseThrow(() -> new RuntimeException("Subject not found with ID: " + subjectId));
    }
}
