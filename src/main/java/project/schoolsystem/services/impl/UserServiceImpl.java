package project.schoolsystem.services.impl;

import project.schoolsystem.controllers.payload.CreateUserRequest;
import project.schoolsystem.controllers.payload.EditUserRequest;
import project.schoolsystem.controllers.payload.ModifyUserResponse;
import project.schoolsystem.dtos.RoleDto;
import project.schoolsystem.dtos.UserDto;
import project.schoolsystem.models.Role;
import project.schoolsystem.models.User;
import project.schoolsystem.repos.UserRepository;
import project.schoolsystem.security.config.JwtTokenUtil;
import project.schoolsystem.security.models.JwtRequest;
import project.schoolsystem.security.models.JwtResponse;
import project.schoolsystem.services.RoleService;
import project.schoolsystem.services.UserService;
import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.persistence.EntityNotFoundException;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

@Service
public class UserServiceImpl implements UserService {

    private final Logger logger = LoggerFactory.getLogger(UserServiceImpl.class);

    public static final String USER_DISABLED = "USER_DISABLED";

    public static final String INVALID_CREDENTIALS = "INVALID_CREDENTIALS";

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private JwtTokenUtil jwtTokenUtil;

    @Autowired
    private RoleService roleService;

    @Autowired
    private PasswordEncoder bcryptEncoder;

    @Autowired
    private ModelMapper modelMapper;


    @Override
    public JwtResponse createAuthenticationToken(JwtRequest authenticationRequest) {
        JwtResponse jwtResponse = new JwtResponse();
        Authentication authentication;
        try {
            authentication = authenticate(authenticationRequest.getUsername(), authenticationRequest.getPassword());
        } catch (DisabledException e) {
            logger.error(USER_DISABLED, e);
            return jwtResponse;
        } catch (BadCredentialsException e) {
            logger.error(INVALID_CREDENTIALS, e);
            return jwtResponse;
        }

        SecurityContextHolder.getContext().setAuthentication(authentication);
        String jwt = jwtTokenUtil.generateJwtToken(authentication);

        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
        Set<String> roles = userDetails.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.toSet());

        jwtResponse.setUserId(userDetails.getId());
        jwtResponse.setToken(jwt);
        jwtResponse.setUsername(userDetails.getUsername());
        jwtResponse.setEmail(userDetails.getEmail());
        jwtResponse.setRoles(roles);
        return jwtResponse;
    }

    @Override
    public ModifyUserResponse save(CreateUserRequest createUserRequest) {
        ModifyUserResponse response = new ModifyUserResponse();
        if (userRepository.existsByUsername(createUserRequest.getUsername())) {
            response.getErrors().add("Username is already taken!");
            return response;
        }

        if (userRepository.existsByEmail(createUserRequest.getEmail())) {
            response.getErrors().add("Email is already in use!");
            return response;
        }

        User newUser = new User();
        newUser.setUsername(createUserRequest.getUsername());
        newUser.setPassword(bcryptEncoder.encode(createUserRequest.getPassword()));
        newUser.setEmail(createUserRequest.getEmail());

        Set<String> strRoles = createUserRequest.getRoles();
        Set<RoleDto> roles = createRoleSet(strRoles);
        newUser.getRoles().addAll(roles.stream().map(r -> modelMapper.map(r, Role.class)).collect(Collectors.toSet()));

        User persisted = userRepository.save(newUser);
        response.setUser(modelMapper.map(persisted, UserDto.class));
        return response;
    }

    @Override
    public ModifyUserResponse update(EditUserRequest editUserRequest) {
        ModifyUserResponse response = new ModifyUserResponse();
        User usr = userRepository.findById(editUserRequest.getId())
                .orElseThrow(() -> new EntityNotFoundException("User not found!"));

        if (userRepository.existsByUsername(editUserRequest.getUsername())) {
            response.getErrors().add("Username is already taken!");
            return response;
        } else {
            if (!editUserRequest.getUsername().isEmpty()) {
                usr.setUsername(editUserRequest.getUsername());
            }
        }

        if (userRepository.existsByEmail(editUserRequest.getEmail())) {
            response.getErrors().add("Email is already in use!");
            return response;
        } else {
            if (!editUserRequest.getEmail().isEmpty()) {
                usr.setEmail(editUserRequest.getEmail());
            }
        }

        if (!editUserRequest.getPassword().isEmpty()) {
            usr.setPassword(bcryptEncoder.encode(editUserRequest.getPassword()));
        }

        if (editUserRequest.getRoles() != null && !editUserRequest.getRoles().isEmpty()) {
            Set<String> strRoles = editUserRequest.getRoles();
            Set<RoleDto> roles = createRoleSet(strRoles);
            usr.setRoles(roles.stream().map(r -> modelMapper.map(r, Role.class)).collect(Collectors.toSet()));
        }

        User updated = userRepository.save(usr);
        response.setUser(modelMapper.map(updated, UserDto.class));
        return response;
    }

    @Async
    @Override
    public CompletableFuture<Void> delete(String userName) {
        Optional<User> toDelete = userRepository.findByUsername(userName);

        return CompletableFuture.runAsync(() -> {
            logger.info("Starting long delete process.");
            try {
                Thread.sleep(10 * 1000);
            } catch (InterruptedException e) {
                logger.error("Interrupted exception....", e);
            }
            toDelete.ifPresent(user -> userRepository.delete(user));
            logger.info("Ending long delete process.");
        });
    }

    @Override
    public List<UserDto> findAllByRole(String role) {
        Optional<RoleDto> roledto = roleService.findByRoleName("ROLE_" + role.toUpperCase());
        if (roledto.isPresent()) {
            List<User> users = userRepository.findAllByRole(roledto.get().getName());
            return users.stream().map(u -> modelMapper.map(u, UserDto.class)).collect(Collectors.toList());
        }
        return null;
    }

    private Authentication authenticate(String username, String password) {
        return authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(username, password));
    }

    private Set<RoleDto> createRoleSet(Set<String> strRoles) {
        Set<RoleDto> roles = new HashSet<>();
        if (strRoles == null || strRoles.isEmpty()) {
            RoleDto userRole = roleService.findByRoleName("ROLE_USER")
                    .orElseThrow(() -> new RuntimeException("Error: Role is not found."));
            roles.add(userRole);
        } else {
            strRoles.forEach(role -> {
                switch (role) {
                    case "student":
                        Optional<RoleDto> studentRole = roleService.findByRoleName("ROLE_STUDENT");
                        studentRole.ifPresent(roles::add);
                        break;
                    case "teacher":
                        Optional<RoleDto> teacherRole = roleService.findByRoleName("ROLE_TEACHER");
                        teacherRole.ifPresent(roles::add);
                        break;
                    default:
                        Optional<RoleDto> userRole = roleService.findByRoleName("ROLE_USER");
                        userRole.ifPresent(roles::add);
                }
            });
        }
        return roles;
    }

}
