package com.example.Testlytics.Repository;

import com.example.Testlytics.Entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Integer> {
    @Modifying
@Query("UPDATE User u SET u.image = :image WHERE u.id = :id")
void updateUserImage(@Param("id") Long id, @Param("image") byte[] image);

    // Fetch all active (non-deleted) users
    @Query("SELECT u FROM User u WHERE u.deletedOn IS NULL")
    List<User> findAllActiveUsers();

    // Fetch active users by role
    // @Query("SELECT u FROM User u WHERE u.deletedOn IS NULL AND u.role.roleName = :roleName")
    // List<User> findAllActiveUsersByRole(@Param("roleName") String roleName);

    // Find user by ID if not deleted
    @Query("SELECT u FROM User u WHERE u.userId = :userId AND u.deletedOn IS NULL")
    Optional<User> findByIdIfNotDeleted(@Param("userId") Integer userId);

    // Find user by username (for authentication)
    Optional<User> findByUsername(String username);

    Optional<User> findByEmail(String email);

    boolean existsByEmail(String email);
  
}
