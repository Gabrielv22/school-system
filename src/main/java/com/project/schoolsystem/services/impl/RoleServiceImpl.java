package com.project.schoolsystem.services.impl;

import com.project.schoolsystem.dtos.RoleDto;
import com.project.schoolsystem.models.Role;
import com.project.schoolsystem.repos.RoleRepository;
import com.project.schoolsystem.services.RoleService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.util.Collections;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class RoleServiceImpl implements RoleService {

    private final Set<RoleDto> roles = new HashSet<>();

    @Autowired
    private ModelMapper modelMapper;

    @Autowired
    private RoleRepository roleRepository;

    @PostConstruct
    void initRoles() {
        roles.addAll(roleRepository.findAll().stream()
                .map(r -> modelMapper.map(r, RoleDto.class))
                .collect(Collectors.toSet()));
    }


    @Override
    public Set<RoleDto> getRoles() {
        return Collections.unmodifiableSet(roles);
    }

    @Override
    public void save(RoleDto roleDto) {
        Role role = roleRepository.save(modelMapper.map(roleDto, Role.class));
        roles.add(modelMapper.map(role, RoleDto.class));
    }

    @Override
    public Optional<RoleDto> findByRoleName(String name) {
        return roles.stream().filter(r -> r.getName().equals(name)).findFirst();
    }
}
