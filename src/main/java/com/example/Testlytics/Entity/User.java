package com.example.Testlytics.Entity;
import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@Table(name = "users")
public class User {
    @Id
    private Integer userId; 

    @Column(nullable = false)
    private String username;

    @Column(unique = true, nullable = false)
    private String email;

    @Column(nullable = false)
    private String password;

    @ManyToOne
    @JoinColumn(name = "role_id", nullable = false)
    private Role role;
    @Lob
    @Column(columnDefinition = "BYTEA") // PostgreSQL uses BYTEA for storing binary data
    private byte[] image;

    private LocalDateTime createdOn = LocalDateTime.now();
    private LocalDateTime modifiedOn;

    @Column(nullable = true) 
    private LocalDateTime deletedOn;

    // Soft delete method
    public void softDelete() {
        this.deletedOn = LocalDateTime.now();
    }

    // Restore method (if needed)
    // public void restore() {
    //     this.deletedOn = null; 
    // }

}

