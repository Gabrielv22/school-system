package project.schoolsystem.services.impl;

import project.schoolsystem.dtos.CourseDto;
import project.schoolsystem.dtos.RoleDto;
import project.schoolsystem.dtos.ScoreDto;
import project.schoolsystem.dtos.UserDto;
import project.schoolsystem.models.Course;
import project.schoolsystem.models.Role;
import project.schoolsystem.models.Score;
import project.schoolsystem.models.User;
import project.schoolsystem.repos.CourseRepository;
import project.schoolsystem.repos.ScoreRepository;
import project.schoolsystem.repos.UserRepository;
import project.schoolsystem.services.CourseService;
import project.schoolsystem.services.RoleService;
import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import javax.persistence.EntityNotFoundException;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

@Service
public class CourseServiceImpl implements CourseService {

    private final Logger logger = LoggerFactory.getLogger(CourseServiceImpl.class);

    @Autowired
    private ModelMapper modelMapper;

    @Autowired
    private CourseRepository courseRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RoleService roleService;

    @Autowired
    private ScoreRepository scoreRepository;

    @Override
    public List<CourseDto> getAllCourses() {
        List<Course> courses = courseRepository.findAll();
        logger.info("Courses found: {}", courses.size());
        return courses
                .stream()
                .map(course -> modelMapper.map(course, CourseDto.class))
                .collect(Collectors.toList());
    }

    @Override
    public CourseDto changeCourseName(Long id, CourseDto courseDto) {
        Optional<Course> optionalCourse = courseRepository.findById(id);
        if (optionalCourse.isPresent()) {
            Course course = optionalCourse.get();
            course.setName(courseDto.getName());
            courseRepository.save(course);
            return modelMapper.map(course, CourseDto.class);
        }
        return null;
    }

    @Override
    public void createCourse(CourseDto dto) {
        courseRepository.save(modelMapper.map(dto, Course.class));
    }


    @Override
    public CourseDto findById(Long id) {
        Optional<Course> courseOptional = courseRepository.findById(id);
        return courseOptional.map(course -> modelMapper.map(course, CourseDto.class)).orElse(null);
    }

    @Async
    @Override
    public CompletableFuture<Void> deleteByName(String name) {
        Optional<Course> toDeleteCourse = courseRepository.findByName(name);
        return CompletableFuture.runAsync(() -> {
            logger.info("Starting Long Deletion Process ...");
            try {
                Thread.sleep(10 * 1000);
            } catch (InterruptedException e) {
                logger.error("Interrupted exception", e);
            }
            toDeleteCourse.ifPresent(course -> courseRepository.delete(course));
            logger.info("Ending Long Deletion process");
        });
    }

    @Override
    public UserDto assignStudentToCourse(String studentUsername, Long courseId) {
        Optional<User> user = userRepository.findByUsername(studentUsername);
        Optional<Course> course = courseRepository.findById(courseId);
        if (user.isPresent() && course.isPresent()) {
            User usr = user.get();
            Course crs = course.get();
            String studentRole = roleService.rgetRoles().stream().filter(roleDto ->
                roleDto.getName().equals("ROLE_TEACHER")).map(RoleDto::getName).findFirst().orElseThrow(()->
                    new EntityNotFoundException("Teacher role not found"));

            if (!isInRole(usr, studentRole)) {
                logger.error("The user is not a student: {}", usr);
                return null;
            }

            usr.getCourses().add(crs);
            return modelMapper.map(userRepository.save(usr), UserDto.class);
        }
        return null;
    }

    @Override
    public ScoreDto assignAScoreToStudentsCourse(String studentUsername, Long courseId, Double scoreValue) {
        Optional<User> user = userRepository.findByUsername(studentUsername);
        Optional<Course> course = courseRepository.findById(courseId);
        if (user.isPresent() && course.isPresent()) {
            User usr = user.get();
            Course crs = course.get();
            String studentRole = roleService.getRoles().stream()
                    .filter(r -> r.getName().equals("ROLE_STUDENT"))
                    .map(RoleDto::getName)
                    .findFirst().orElseThrow(() -> new EntityNotFoundException("Student Role not found!"));

            if (!isInRole(usr, studentRole)) {
                logger.error("The user is not a student: {}", usr);
                return null;
            }

            if (!isInCourse(usr, crs)) {
                logger.error("The user: {} is not in the course: {}", usr, crs);
                return null;
            }

            Score score = new Score();
            score.setCourse(crs);
            score.setUser(usr);
            score.setValue(scoreValue);
            score.setDate(LocalDate.now());

            return modelMapper.map(scoreRepository.save(score), ScoreDto.class);
        }
        return null;
    }

    private boolean isInRole(User usr, String studentRole) {
        Optional<Role> role = usr.getRoles().stream()
                .filter(r -> r.getName().equals(studentRole))
                .findFirst();
        return role.isPresent();
    }

    private boolean isInCourse(User usr, Course crs) {
        Optional<Course> course = usr.getCourses().stream()
                .filter(c -> c.getId() == crs.getId())
                .findFirst();

        return course.isPresent();
    }
}
