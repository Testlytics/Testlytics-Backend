package com.example.Testlytics.Controller;

import com.example.Testlytics.DTO.ApiResponse;
import com.example.Testlytics.DTO.SubjectDTO;
import com.example.Testlytics.Entity.Subject;
import com.example.Testlytics.Service.SubjectService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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

    // ✅ POST: Create a new subject
    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<SubjectDTO>> createSubject(@RequestBody SubjectDTO subjectDTO) {
        Subject subject = new Subject();
        subject.setSubjectName(subjectDTO.getSubjectName());

        Subject savedSubject = subjectService.createSubject(subject);
        SubjectDTO responseDTO = new SubjectDTO(savedSubject.getSubjectId(), savedSubject.getSubjectName());

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(new ApiResponse<>(201, "Created", "Subject created successfully", responseDTO));
    }

    // ✅ GET: Retrieve all subjects
    @GetMapping
    @PreAuthorize("hasAnyRole('STUDENT', 'ADMIN')")
    public ResponseEntity<ApiResponse<List<SubjectDTO>>> getAllSubjects() {
        List<SubjectDTO> subjects = subjectService.getAllSubjects().stream()
                .map(subject -> new SubjectDTO(subject.getSubjectId(), subject.getSubjectName()))
                .collect(Collectors.toList());

        return ResponseEntity.ok(new ApiResponse<>(200, "OK", "Subjects retrieved successfully", subjects));
    }

    // ✅ GET: Retrieve details of a specific subject by subjectId
    @GetMapping("/{subjectId}")
    @PreAuthorize("hasAnyRole('STUDENT', 'ADMIN')")
    public ResponseEntity<ApiResponse<SubjectDTO>> getSubjectById(@PathVariable UUID subjectId) {
        Subject subject = subjectService.getSubjectById(subjectId);
        SubjectDTO subjectDTO = new SubjectDTO(subject.getSubjectId(), subject.getSubjectName());

        return ResponseEntity.ok(new ApiResponse<>(200, "OK", "Subject retrieved successfully", subjectDTO));
    }
}
