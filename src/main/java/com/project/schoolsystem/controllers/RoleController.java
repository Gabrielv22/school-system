package com.project.schoolsystem.controllers;

import com.project.schoolsystem.dtos.RoleDto;
import com.project.schoolsystem.services.RoleService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class RoleController {

    private final Logger logger = LoggerFactory.getLogger(RoleController.class);

    @Autowired
    private RoleService roleService;

    @PostMapping("/role")
    @PreAuthorize("hasRole('TEACHER')")
    public ResponseEntity<Void> create(@RequestBody RoleDto role) {
        logger.info("Creating new role: {}", role);
        roleService.save(role);
        return new ResponseEntity<>(HttpStatus.OK);
    }

}
