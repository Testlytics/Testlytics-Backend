package com.example.Testlytics.Entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "test")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Test {

    @Id
    @GeneratedValue
    @Column(name = "test_id", updatable = false, nullable = false)
    private UUID testId;

    @Column(name = "subject_id", nullable = false)
    private UUID subjectId;

    @Column(name = "test_name", nullable = false)
    private String testName;

    @Column(name = "test_date", nullable = false)
    private LocalDate testDate;

    @Column(name = "test_duration", nullable = false)
    private int testDuration;

    @Column(name = "start_time", nullable = false)
    @JsonFormat(pattern = "HH:mm:ss")
    private LocalTime startTime;

    @Column(name = "end_time", nullable = false)
    @JsonFormat(pattern = "HH:mm:ss")
    private LocalTime endTime;

    @CreationTimestamp
    @Column(name = "created_on", updatable = false)
    private LocalDateTime createdOn;

    @UpdateTimestamp
    @Column(name = "modified_on")
    private LocalDateTime modifiedOn;

    @Column(name = "deleted_on")
    private LocalDateTime deletedOn;
     
    @Column(name = "is_published", nullable = false, columnDefinition = "boolean default false")
    @Builder.Default 
    private boolean isPublished = false; 

    public boolean isDeleted() {
        return deletedOn != null;
    }

    // ✅ Check if the test is active (ongoing)
    public boolean isActive() {
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime startDateTime = LocalDateTime.of(testDate, startTime);
        LocalDateTime endDateTime = LocalDateTime.of(testDate, endTime);
        return now.isAfter(startDateTime) && now.isBefore(endDateTime);
    }

    // ✅ Check if the test is completed (past end time)
    public boolean isCompleted() {
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime endDateTime = LocalDateTime.of(testDate, endTime);
        return now.isAfter(endDateTime);
    }

    // ✅ Check if the test is upcoming (future test)
    public boolean isUpcoming() {
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime startDateTime = LocalDateTime.of(testDate, startTime);
        return now.isBefore(startDateTime);
    }
}
