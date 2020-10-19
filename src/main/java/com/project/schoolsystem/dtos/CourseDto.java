package com.project.schoolsystem.dtos;

import com.project.schoolsystem.models.User;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Set;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CourseDto {

    private Long id;

    private String name;

    private Set<User> students;

}
