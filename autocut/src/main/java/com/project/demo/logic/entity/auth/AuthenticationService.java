package com.project.demo.logic.entity.auth;

import com.project.demo.logic.entity.user.User;
import com.project.demo.logic.entity.user.UserRepository;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.stereotype.Service;

@Service
public class AuthenticationService {
    private final UserRepository userRepository;
    private final AuthenticationManager authenticationManager;

    public AuthenticationService(
            UserRepository userRepository,
            AuthenticationManager authenticationManager
    ) {
        this.authenticationManager = authenticationManager;
        this.userRepository = userRepository;
    }

    public User authenticate(User input) {

        User user = userRepository.findByEmail(input.getEmail())
                .orElseThrow(() -> new RuntimeException("Credenciales inválidas"));

        if (!user.isActive()) {
            throw new RuntimeException("La cuenta está inactiva");
        }

        try {
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            input.getEmail(),
                            input.getPassword()
                    )
            );
        } catch (BadCredentialsException e) {
            throw new RuntimeException("Credenciales inválidas");
        }

        return user;
    }
}
