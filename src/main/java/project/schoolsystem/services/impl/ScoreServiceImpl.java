package project.schoolsystem.services.impl;

import project.schoolsystem.dtos.ScoreDto;
import project.schoolsystem.models.Course;
import project.schoolsystem.models.Score;
import project.schoolsystem.models.User;
import project.schoolsystem.repos.CourseRepository;
import project.schoolsystem.repos.ScoreRepository;
import project.schoolsystem.repos.UserRepository;
import project.schoolsystem.services.ScoreService;
import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

@Service
public class ScoreServiceImpl implements ScoreService {

    private final Logger logger = LoggerFactory.getLogger(ScoreServiceImpl.class);

    @Autowired
    private ModelMapper modelMapper;

    @Autowired
    private ScoreRepository scoreRepository;

    @Autowired
    private CourseRepository courseRepository;

    @Autowired
    private UserRepository userRepository;

    @Override
    public List<ScoreDto> getAllScores() {
        return this.scoreRepository.findAll()
                .stream()
                .map(score -> modelMapper.map(score, ScoreDto.class))
                .collect(Collectors.toList());
    }

    @Override
    public ScoreDto getScoreById(Long id) {
        Optional<Score> optionalScore = scoreRepository.findById(id);
        return optionalScore.map(score -> modelMapper.map(score, ScoreDto.class)).orElse(null);
    }

    @Override
    public ScoreDto changeScore(ScoreDto scoreDto) {
        ScoreDto score = getScoreById(scoreDto.getId());
        if (score != null) {
            score.setValue(scoreDto.getValue());
            scoreRepository.save(modelMapper.map(score, Score.class));
            return score;
        }
        return null;
    }

    @Override
    public ScoreDto assignScoreToStudent(String courseName, String studentName, Double score) {
        Optional<Course> optionalCourse = courseRepository.findByName(courseName);
        Optional<User> optionalUser = userRepository.findByUsername(studentName);
        if (optionalCourse.isPresent() && optionalUser.isPresent()) {
            User student = userRepository.checkIfUserIsInCourse(optionalUser.get().getId());
            logger.info("Course & Student found. Creating score in progress ...");
            Course course = optionalCourse.get();
            ScoreDto scoreDto = new ScoreDto();
            scoreDto.setValue(score);
            scoreDto.setCourse(course);
            scoreDto.setUser(student);
            scoreRepository.save(modelMapper.map(scoreDto, Score.class));
            return scoreDto;
        }
        logger.info("Course/Student not FOUND. Please check your request once again");
        return null;
    }


    @Async
    @Override
    public CompletableFuture<Void> deleteScoreById(Long scoreId) {
        Optional<Score> toDelete = scoreRepository.findById(scoreId);
        return CompletableFuture.runAsync(() -> {
            logger.info("Starting a long process for deletion a course");
            try {
                Thread.sleep(10 * 1000);
            } catch (InterruptedException e) {
                logger.error("Interrupted Exception occured");
            }
            toDelete.ifPresent(score -> scoreRepository.delete(score));
        });
    }

    @Override
    public Double getAverageScoreAcrossStudents() {
        logger.info("Getting average score across all courses/students");
        return scoreRepository.findAverageScoreAllStudents();
    }

    @Override
    public Double getAverageByStudentId(Long studentId) {
        logger.info("Getting average score for student: {}", studentId);
        return scoreRepository.findAverageScoreByStudent(studentId);
    }

    @Override
    public Double getAverageByStudentIdForSpecificCourse(Long studentId, Long courseId) {
        logger.info("Getting average score for student id: {} and course id: {}", studentId, courseId);
        return scoreRepository.findAverageScoreByStudentForSpecificCourse(studentId, courseId);
    }

    @Override
    public Double getAverageScoreForAllStudentsInGivenCourse(Long courseId) {
        logger.info("Getting average score across all students for course id: {}", courseId);
        return scoreRepository.findAverageScoreForGivenCourse(courseId);
    }

}
