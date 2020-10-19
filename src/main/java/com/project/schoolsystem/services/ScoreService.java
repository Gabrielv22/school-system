package com.project.schoolsystem.services;

import com.project.schoolsystem.dtos.ScoreDto;

import java.util.List;
import java.util.concurrent.CompletableFuture;

public interface ScoreService {

    List<ScoreDto> getAllScores();

    ScoreDto getScoreById(Long id);

    ScoreDto changeScore(ScoreDto score);

    ScoreDto assignScoreToStudent(String courseName, String studentName, Double score);

    CompletableFuture<Void> deleteScoreById(Long studentId);

    Double getAverageScoreAcrossStudents();

    Double getAverageByStudentId(Long id);

    Double getAverageByStudentIdForSpecificCourse(Long studentId, Long courseId);

    Double getAverageScoreForAllStudentsInGivenCourse(Long courseId);
}
