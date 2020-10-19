package com.project.schoolsystem.services;

import com.project.schoolsystem.dtos.RoleDto;

import java.util.Optional;
import java.util.Set;

public interface RoleService {

    Set<RoleDto> getRoles();

    void save(RoleDto roleDto);

    Optional<RoleDto> findByRoleName(String name);

}
