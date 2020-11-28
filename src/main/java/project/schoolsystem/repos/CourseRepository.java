package project.schoolsystem.repos;

import project.schoolsystem.models.Course;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;


public interface CourseRepository extends JpaRepository<Course, Long> {

    Optional<Course> findByName(String name);

}
