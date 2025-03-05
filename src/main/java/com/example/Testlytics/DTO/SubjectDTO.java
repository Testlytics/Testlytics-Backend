package com.example.Testlytics.DTO;


import java.util.UUID;

public class SubjectDTO {
    private UUID subjectId;
    private String subjectName;

    public SubjectDTO() {}

    public SubjectDTO(UUID subjectId, String subjectName) {
        this.subjectId = subjectId;
        this.subjectName = subjectName;
    }

    public UUID getSubjectId() {
        return subjectId;
    }

    public void setSubjectId(UUID subjectId) {
        this.subjectId = subjectId;
    }

    public String getSubjectName() {
        return subjectName;
    }

    public void setSubjectName(String subjectName) {
        this.subjectName = subjectName;
    }
}
