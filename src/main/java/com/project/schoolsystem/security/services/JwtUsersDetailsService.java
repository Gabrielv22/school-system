package com.project.schoolsystem.security.services;

import com.project.schoolsystem.models.User;
import com.project.schoolsystem.repos.UserRepository;
import com.project.schoolsystem.services.impl.UserDetailsImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class JwtUsersDetailsService implements UserDetailsService {

    public static final String ROLE_USER = "ROLE_USER";

    @Autowired
    private UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user =
                userRepository.findByUsername(username).
                        orElseThrow(()->new UsernameNotFoundException("User not Found with username " + username));
        return UserDetailsImpl.build(user);
    }
}
