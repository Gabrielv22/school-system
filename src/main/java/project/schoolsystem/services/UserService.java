package project.schoolsystem.services;

import project.schoolsystem.controllers.payload.CreateUserRequest;
import project.schoolsystem.controllers.payload.EditUserRequest;
import project.schoolsystem.controllers.payload.ModifyUserResponse;
import project.schoolsystem.dtos.UserDto;
import project.schoolsystem.security.models.JwtRequest;
import project.schoolsystem.security.models.JwtResponse;

import java.util.List;
import java.util.concurrent.CompletableFuture;

public interface UserService {

    JwtResponse createAuthenticationToken(JwtRequest authenticationRequest);

    ModifyUserResponse save(CreateUserRequest createUserRequest);

    ModifyUserResponse update(EditUserRequest editUserRequest);

    CompletableFuture<Void> delete(String userName);

    List<UserDto> findAllByRole(String role);
}
