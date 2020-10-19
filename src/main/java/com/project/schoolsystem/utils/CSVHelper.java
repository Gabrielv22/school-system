package com.project.schoolsystem.utils;

import com.project.schoolsystem.dtos.CourseDto;
import com.project.schoolsystem.dtos.UserDto;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

public class CSVHelper {

    private static final String TYPE = "text/csv";

    public static boolean hasCSVFormat(MultipartFile multipartFile) {
        return TYPE.equals(multipartFile.getContentType());
    }

    public static List<CourseDto> csvToCourses(InputStream is) {
        try (BufferedReader fileReader = new BufferedReader(new InputStreamReader(is, StandardCharsets.UTF_8));
             CSVParser csvParser = new CSVParser(fileReader,
                     CSVFormat.DEFAULT.withFirstRecordAsHeader().withIgnoreHeaderCase().withTrim())) {

            List<CourseDto> courses = new ArrayList<>();

            Iterable<CSVRecord> csvRecords = csvParser.getRecords();

            for (CSVRecord csvRecord : csvRecords) {
                CourseDto courseDto = new CourseDto();
                String id = csvRecord.get("id");
                courseDto.setId(Long.parseLong(id));
                courseDto.setName(csvRecord.get("username"));
                courses.add(courseDto);
            }
            return courses;
        } catch (IOException e) {
            throw new RuntimeException("failed to parse CSV file: " + e.getMessage());
        }
    }

}
