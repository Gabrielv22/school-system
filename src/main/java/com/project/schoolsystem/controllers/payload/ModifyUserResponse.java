package com.project.schoolsystem.controllers.payload;

import com.project.schoolsystem.dtos.UserDto;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.HashSet;
import java.util.Set;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ModifyUserResponse {

    private UserDto user;
    private Set<String> errors = new HashSet<>();

}
