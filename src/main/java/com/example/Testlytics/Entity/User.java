package com.example.Testlytics.Entity;
import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "users")
public class User {
    @Id
    private Integer userId; // Unique 4-digit ID

    @Column(unique = true, nullable = false)
    private String username;

    @Column(unique = true, nullable = false)
    private String email;

    @Column(nullable = false)
    private String password;

    @ManyToOne
    @JoinColumn(name = "role_id", nullable = false)
    private Role role;

    private String image;

    private LocalDateTime createdOn = LocalDateTime.now();
    private LocalDateTime modifiedOn;

    @Column(nullable = true) // Soft delete column
    private LocalDateTime deletedOn;

    // Soft delete method
    public void softDelete() {
        this.deletedOn = LocalDateTime.now(); // Mark as deleted
    }

    // Restore method (if needed)
    public void restore() {
        this.deletedOn = null; // Restore user
    }

}

