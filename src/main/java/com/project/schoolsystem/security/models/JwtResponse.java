package com.project.schoolsystem.security.models;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Set;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class JwtResponse {

    private String token;
    private Long userId;
    private String username;
    private String email;
    private Set<String> roles;

}
