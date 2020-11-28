package project.schoolsystem.services;

import project.schoolsystem.dtos.UserDto;

import java.util.List;

public interface MyUserService {

    UserDto findUserByUsername(String name);

    UserDto findStudentById(Long id);

    List<UserDto> findAllUsersByRole(String role);

}
