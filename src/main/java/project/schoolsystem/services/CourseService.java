package project.schoolsystem.services;

import project.schoolsystem.dtos.CourseDto;
import project.schoolsystem.dtos.ScoreDto;
import project.schoolsystem.dtos.UserDto;

import java.util.List;
import java.util.concurrent.CompletableFuture;

public interface CourseService {

    List<CourseDto> getAllCourses();

    CourseDto changeCourseName(Long id, CourseDto courseDto);

    void createCourse(CourseDto courseDto);

    CourseDto findById(Long id);

    CompletableFuture<Void> deleteByName(String name);

    UserDto assignStudentToCourse(String studentUsername, Long courseId);

    ScoreDto assignAScoreToStudentsCourse(String studentUsername, Long courseId, Double scoreValue);

}
