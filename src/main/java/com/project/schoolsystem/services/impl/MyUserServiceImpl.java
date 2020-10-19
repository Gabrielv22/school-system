package com.project.schoolsystem.services.impl;

import com.project.schoolsystem.dtos.UserDto;
import com.project.schoolsystem.models.User;
import com.project.schoolsystem.repos.UserRepository;
import com.project.schoolsystem.services.MyUserService;
import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;
import java.util.Optional;

public class MyUserServiceImpl implements MyUserService {

    private final Logger logger = LoggerFactory.getLogger(MyUserServiceImpl.class);

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ModelMapper modelMapper;

    @Override
    public UserDto findUserByUsername(String name) {
        Optional<User> byUsername = userRepository.findByUsername(name);
        if(byUsername.isPresent()){
            User user = byUsername.get();
            return modelMapper.map(user,UserDto.class);
        }
        return null;
    }

    @Override
    public UserDto findStudentById(Long id) {
        return null;
    }

    @Override
    public List<UserDto> findAllUsersByRole(String role) {
        return null;
    }
}
