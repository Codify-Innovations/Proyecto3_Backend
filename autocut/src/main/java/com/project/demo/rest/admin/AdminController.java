package com.project.demo.rest.admin;

import com.project.demo.logic.entity.http.GlobalResponseHandler;
import com.project.demo.logic.entity.rol.Role;
import com.project.demo.logic.entity.rol.RoleEnum;
import com.project.demo.logic.entity.rol.RoleRepository;
import com.project.demo.logic.entity.user.User;
import com.project.demo.logic.entity.user.UserRepository;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Optional;

@RequestMapping("/admin")
@RestController
public class AdminController {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @PostMapping
    @PreAuthorize("hasRole('SUPER_ADMIN_ROLE')")
    public ResponseEntity<?> createAdministrator(@RequestBody User newAdminUser, HttpServletRequest request) {
        Optional<Role> optionalRole = roleRepository.findByName(RoleEnum.SUPER_ADMIN_ROLE);

        if (optionalRole.isEmpty()) {
            return new GlobalResponseHandler().handleResponse(
                    "Admin role not found in system",
                    HttpStatus.INTERNAL_SERVER_ERROR,
                    request
            );
        }

        var user = new User();
        user.setName(newAdminUser.getName());
        user.setEmail(newAdminUser.getEmail());
        user.setPassword(passwordEncoder.encode(newAdminUser.getPassword()));
        user.setRole(optionalRole.get());

        User savedUser = userRepository.save(user);

        return new GlobalResponseHandler().handleResponse(
                "Administrator created successfully",
                savedUser,
                HttpStatus.CREATED,
                request
        );
    }


}
