package com.project.schoolsystem.dtos;

import com.project.schoolsystem.models.Course;
import com.project.schoolsystem.models.User;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ScoreDto {

    private Long id;

    private Double value;

    private LocalDate date;

    private User user;

    private Course course;

}
