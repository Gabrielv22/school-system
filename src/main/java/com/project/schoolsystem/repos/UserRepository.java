package com.project.schoolsystem.repos;

import com.project.schoolsystem.models.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;


public interface UserRepository extends JpaRepository<User, Long> {

    Optional<User> findByUsername(String name);

    @Query("SELECT u FROM User u INNER JOIN u.roles r WHERE r.name = :role")
    List<User> findAllByRole(String role);

    @Query("SELECT u FROM User u INNER JOIN u.roles r WHERE r.name = 'STUDENT' AND u.id = :id")
    Optional<User> findStudentById(Long id);

    @Query("SELECT u FROM User u INNER JOIN u.courses c WHERE c.id = :userId")
    User checkIfUserIsInCourse(Long userId);

    Boolean existsByUsername(String username);

    Boolean existsByEmail(String email);
}
