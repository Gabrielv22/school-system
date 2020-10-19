package com.project.schoolsystem.services;

import com.project.schoolsystem.controllers.payload.CreateUserRequest;
import com.project.schoolsystem.controllers.payload.EditUserRequest;
import com.project.schoolsystem.controllers.payload.ModifyUserResponse;
import com.project.schoolsystem.dtos.UserDto;
import com.project.schoolsystem.security.models.JwtRequest;
import com.project.schoolsystem.security.models.JwtResponse;

import java.util.List;
import java.util.concurrent.CompletableFuture;

public interface UserService {

    JwtResponse createAuthenticationToken(JwtRequest authenticationRequest);

    ModifyUserResponse save(CreateUserRequest createUserRequest);

    ModifyUserResponse update(EditUserRequest editUserRequest);

    CompletableFuture<Void> delete(String userName);

    List<UserDto> findAllByRole(String role);
}
