package com.project.schoolsystem.controllers;

import com.project.schoolsystem.dtos.ScoreDto;
import com.project.schoolsystem.services.ScoreService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.concurrent.CompletableFuture;

@RestController
@RequestMapping("/scores")
public class ScoreController {

    private final Logger logger = LoggerFactory.getLogger(CourseController.class);

    @Autowired
    private ScoreService scoreService;

    @GetMapping("/all")
    @PreAuthorize("hasRole('TEACHER')")
    public ResponseEntity<List<ScoreDto>> getAllScores() {
        return new ResponseEntity<>(scoreService.getAllScores(), HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ScoreDto> getScoreById(@PathVariable Long id) {
        return new ResponseEntity<>(scoreService.getScoreById(id), HttpStatus.FOUND);
    }

    @PutMapping("/edit")
    public ResponseEntity<ScoreDto> changeValueScore(@RequestBody ScoreDto score) {
        return new ResponseEntity<>(scoreService.changeScore(score), HttpStatus.OK);
    }

    @PostMapping("/add/course/{courseName}/student/{studentName}/score/{scoreValue}")
    public ResponseEntity<Void> assignScoreToStudentToCourse(@PathVariable String courseName,
                                                             @PathVariable String studentName,
                                                             @PathVariable Double scoreValue) {
        logger.info("Assigning a score:{} to a student id:{} in a course ID:{}", scoreValue, studentName, courseName);
        return new ResponseEntity<>(scoreService.assignScoreToStudent(courseName, studentName, scoreValue) != null ? HttpStatus.ACCEPTED
                : HttpStatus.EXPECTATION_FAILED);
    }

    @DeleteMapping("delete/{scoreId}")
    public ResponseEntity<CompletableFuture<Void>> deleteScoreForStudentsByGivenCourse(@PathVariable Long scoreId) {
        return new ResponseEntity<>(scoreService.deleteScoreById(scoreId), HttpStatus.NO_CONTENT);
    }

    @GetMapping("/average/allstudents")
    public ResponseEntity<Double> getAverageScoreAcrossAllStudents() {
        return new ResponseEntity<>(scoreService.getAverageScoreAcrossStudents(), HttpStatus.OK);
    }

    @GetMapping("/average/student/{studentId}")
    public ResponseEntity<Double> getAverageScoreByStudent(@PathVariable Long studentId) {
        return new ResponseEntity<>(scoreService.getAverageByStudentId(studentId), HttpStatus.OK);
    }

    @GetMapping("/average/student/{studentId}/course/{courseId}")
    public ResponseEntity<Double> getAverageScoreByStudentForSpecificCourse(@PathVariable Long studentId,
                                                                            @PathVariable Long courseId) {
        return new ResponseEntity<>(scoreService.getAverageByStudentIdForSpecificCourse(studentId, courseId), HttpStatus.OK);
    }

    @GetMapping("/average/allstudents/course/{courseId}")
    public ResponseEntity<Double> getAverageScoreForStudentsInGivenCourse(@PathVariable Long courseId) {
        return new ResponseEntity<>(scoreService.getAverageScoreForAllStudentsInGivenCourse(courseId), HttpStatus.OK);
    }
}
