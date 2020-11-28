package project.schoolsystem.controllers;

import project.schoolsystem.dtos.CourseDto;
import project.schoolsystem.dtos.UserDto;
import project.schoolsystem.services.CourseService;
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
@RequestMapping("/courses")
public class CourseController {

    private final Logger logger = LoggerFactory.getLogger(CourseController.class);

    @Autowired
    private CourseService courseService;


    @PostMapping(value = "/create")
    @PreAuthorize("hasRole('TEACHER')")
    public ResponseEntity<Void> createCourse(@RequestBody CourseDto course) {
        logger.info("Creating a new course {}", course);
        courseService.createCourse(course);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @PostMapping("/course/{courseId}/student/{studentUsername}")
    @PreAuthorize("hasRole('TEACHER')")
    public ResponseEntity<UserDto> assignStudentToCourse(@PathVariable Long courseId, @PathVariable String studentUsername) {
        logger.info("Assigning student Username:{} to a course ID:{}", studentUsername, courseId);
        UserDto result = courseService.assignStudentToCourse(studentUsername, courseId);
        return new ResponseEntity<>(result, result != null ? HttpStatus.OK : HttpStatus.BAD_REQUEST);
    }

    @GetMapping(value = "/all")
    public ResponseEntity<List<CourseDto>> getAllCourses() {
        logger.info("Getting all courses available");
        return new ResponseEntity<>(courseService.getAllCourses(), HttpStatus.OK);
    }

    @GetMapping(value = "/{id}")
    public ResponseEntity<CourseDto> findCourseById(@PathVariable Long id) {
        logger.info("Searching for course with id {}", id);
        return new ResponseEntity<>(courseService.findById(id), HttpStatus.OK);
    }

    @PutMapping(value = "edit/{id}")
    public ResponseEntity<CourseDto> changeNameCourse(@PathVariable Long id,
                                                      @RequestBody CourseDto courseDto) {
        return new ResponseEntity<>(courseService.changeCourseName(id, courseDto), HttpStatus.OK);
    }

    @DeleteMapping(value = "/delete")
    public ResponseEntity<CompletableFuture<Void>> deleteCourse(@RequestParam String courseName) {
        return new ResponseEntity<>(courseService.deleteByName(courseName),HttpStatus.NO_CONTENT);
    }
}
