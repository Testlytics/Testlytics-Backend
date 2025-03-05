package com.example.Testlytics.Controller;

import com.example.Testlytics.DTO.SubjectDTO;
import com.example.Testlytics.Entity.Subject;
import com.example.Testlytics.Service.SubjectService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/subjects")
public class SubjectController {

    @Autowired
    private SubjectService subjectService;

    // POST: Create a new subject with subject_name in the request body
    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public SubjectDTO createSubject(@RequestBody SubjectDTO subjectDTO) {
        Subject subject = new Subject();
        subject.setSubjectName(subjectDTO.getSubjectName());

        Subject savedSubject = subjectService.createSubject(subject);
        return new SubjectDTO(savedSubject.getSubjectId(), savedSubject.getSubjectName());
    }

    // GET: Retrieve all subjects
    @GetMapping
    @PreAuthorize("hasAnyRole('STUDENT', 'ADMIN')")
    public List<SubjectDTO> getAllSubjects() {
        return subjectService.getAllSubjects().stream()
                .map(subject -> new SubjectDTO(subject.getSubjectId(), subject.getSubjectName()))
                .collect(Collectors.toList());
    }

    // GET: Retrieve details of a specific subject by subjectId
    @GetMapping("/{subjectId}")
    @PreAuthorize("hasAnyRole('STUDENT', 'ADMIN')")
    public SubjectDTO getSubjectById(@PathVariable UUID subjectId) {
        Subject subject = subjectService.getSubjectById(subjectId);
        return new SubjectDTO(subject.getSubjectId(), subject.getSubjectName());
    }


}
