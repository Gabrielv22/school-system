package com.project.schoolsystem.controllers.payload;

import lombok.Data;
import lombok.NonNull;

import java.util.HashSet;
import java.util.Set;

@Data
public class EditUserRequest {
    @NonNull
    private Long id;
    private String username;
    private String email;
    private String password;
    private Set<String> roles = new HashSet<>();
}
