package com.project.schoolsystem.controllers;

import com.project.schoolsystem.controllers.payload.CreateUserRequest;
import com.project.schoolsystem.controllers.payload.EditUserRequest;
import com.project.schoolsystem.controllers.payload.ModifyUserResponse;
import com.project.schoolsystem.security.models.JwtRequest;
import com.project.schoolsystem.dtos.UserDto;
import com.project.schoolsystem.security.models.JwtResponse;
import com.project.schoolsystem.services.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Async;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class UserController {

    private final Logger logger = LoggerFactory.getLogger(UserController.class);

    @Autowired
    private UserService userService;


    @PostMapping(value = "/authenticate")
    public ResponseEntity<JwtResponse> createAuthenticationToken(@RequestBody JwtRequest authenticationRequest) {
       return new ResponseEntity<>(userService.createAuthenticationToken(authenticationRequest),HttpStatus.OK);
    }

    @PostMapping("/register")
    public ResponseEntity<ModifyUserResponse> saveUser(@RequestBody CreateUserRequest user) {
        try {
            return new ResponseEntity<>(userService.save(user), HttpStatus.OK);
        } catch (AuthenticationServiceException ex) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    @PutMapping("/user/update")
    @PreAuthorize("hasRole('TEACHER')")
    public ResponseEntity<ModifyUserResponse> updateUser(@RequestBody EditUserRequest user) {
        try {
            return new ResponseEntity<>(userService.update(user), HttpStatus.OK);
        } catch (AuthenticationServiceException ex) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    @Async
    @DeleteMapping("/user/{userName}/delete")
    @PreAuthorize("hasRole('TEACHER')")
    public ResponseEntity<Void> deleteUser(@PathVariable String userName) {
        try {
            logger.info("Delete User API started.");
            userService.delete(userName);
            logger.info("Returning Http Status code OK (200) before the actual delete.");
            return new ResponseEntity<>(HttpStatus.OK);
        } catch (AuthenticationServiceException ex) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping("/users/{role}")
    @PreAuthorize("hasRole('TEACHER')")
    public ResponseEntity<List<UserDto>> updateUser(@PathVariable String role) {
        try {
            return new ResponseEntity<>(userService.findAllByRole(role), HttpStatus.OK);
        } catch (AuthenticationServiceException ex) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping(value = "users/all")
    public ResponseEntity<List<UserDto>> findAllUsersByRole(@RequestParam String role) {
        return new ResponseEntity<>(userService.findAllByRole(role), HttpStatus.OK);
    }

}

