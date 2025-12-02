package com.project.demo.logic.entity.auth;

import com.google.api.client.googleapis.auth.oauth2.GoogleIdToken;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdTokenVerifier;
import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.project.demo.logic.entity.rol.Role;
import com.project.demo.logic.entity.rol.RoleEnum;
import com.project.demo.logic.entity.rol.RoleRepository;
import com.project.demo.logic.entity.user.User;
import com.project.demo.logic.entity.user.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.Optional;
import java.util.UUID;

@Service
public class GoogleAuthService {

    @Value("${google.client.id}")
    private String googleClientId;

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    public GoogleAuthService(UserRepository userRepository, RoleRepository roleRepository) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
    }

    public User verifyAndGetUser(String idTokenString) throws Exception {
        var jsonFactory = JacksonFactory.getDefaultInstance();

        var transport = GoogleNetHttpTransport.newTrustedTransport();

        GoogleIdTokenVerifier verifier = new GoogleIdTokenVerifier.Builder(transport, jsonFactory)
                .setAudience(Collections.singletonList(googleClientId))
                .build();

        GoogleIdToken idToken = verifier.verify(idTokenString);

        if (idToken == null) {
            throw new IllegalArgumentException("Invalid Google ID token");
        }

        GoogleIdToken.Payload payload = idToken.getPayload();

        String email = payload.getEmail();
        String name = (String) payload.get("name");

        Optional<User> existingUser = userRepository.findByEmail(email);
        if (existingUser.isPresent()) {
            return existingUser.get();
        }

        User newUser = new User();
        newUser.setEmail(email);
        newUser.setName(name);
        newUser.setPassword(passwordEncoder.encode(UUID.randomUUID().toString()));

        Optional<Role> userRole = roleRepository.findByName(RoleEnum.USER);
        userRole.ifPresent(newUser::setRole);

        return userRepository.save(newUser);
    }
}
