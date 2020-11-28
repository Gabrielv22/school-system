package project.schoolsystem.services.impl;

import project.schoolsystem.dtos.CourseDto;
import project.schoolsystem.models.Course;
import project.schoolsystem.repos.CourseRepository;
import project.schoolsystem.services.CSVService;
import project.schoolsystem.utils.CSVHelper;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class CSVServiceImpl implements CSVService {

    @Autowired
    private CourseRepository courseRepository;

    @Autowired
    private ModelMapper mapper;

    @Override
    public void uploadCSV(MultipartFile csvFile) {
        try {
            List<CourseDto> courseDtos = CSVHelper.csvToCourses(csvFile.getInputStream());
            List<Course> courses =
                    courseDtos.stream()
                            .map(userDto -> mapper.map(courseDtos, Course.class))
                            .collect(Collectors.toList());
            courseRepository.saveAll(courses);
        } catch (IOException e) {
            throw new RuntimeException("Failed to store csv data");
        }
    }
}
