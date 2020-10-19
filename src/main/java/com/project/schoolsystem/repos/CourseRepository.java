package com.project.schoolsystem.repos;

import com.project.schoolsystem.models.Course;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;


public interface CourseRepository extends JpaRepository<Course, Long> {

    Optional<Course> findByName(String name);

}
