package com.project.schoolsystem.repos;

import com.project.schoolsystem.models.Score;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;



public interface ScoreRepository extends JpaRepository<Score, Long> {

    @Query("SELECT AVG(value) FROM Score s")
    Double findAverageScoreAllStudents();

    @Query("SELECT AVG(value) FROM Score s WHERE s.user.id = :studentId")
    Double findAverageScoreByStudent(Long studentId);

    @Query("SELECT AVG(value) FROM Score s WHERE s.user.id = :studentId AND s.course.id = :courseId")
    Double findAverageScoreByStudentForSpecificCourse(Long studentId,Long courseId);

    @Query("SELECT AVG(value) FROM Score s WHERE s.course.id = :courseId")
    Double findAverageScoreForGivenCourse(Long courseId);

}
