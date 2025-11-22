package com.project.demo.rest.user;

import com.project.demo.logic.entity.http.GlobalResponseHandler;
import com.project.demo.logic.entity.http.Meta;
import com.project.demo.logic.entity.user.User;
import com.project.demo.logic.entity.user.UserRepository;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("/users")
public class UserRestController {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @GetMapping
    @PreAuthorize("hasAnyRole('SUPER_ADMIN_ROLE')")
    public ResponseEntity<?> getAll(
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int size,
            HttpServletRequest request) {

        Pageable pageable = PageRequest.of(page - 1, size);
        Page<User> usersPage = userRepository.findAll(pageable);

        Meta meta = new Meta(request.getMethod(), request.getRequestURL().toString());
        meta.setTotalPages(usersPage.getTotalPages());
        meta.setTotalElements(usersPage.getTotalElements());
        meta.setPageNumber(usersPage.getNumber() + 1);
        meta.setPageSize(usersPage.getSize());

        return new GlobalResponseHandler().handleResponse(
                "Users retrieved successfully",
                usersPage.getContent(),
                HttpStatus.OK,
                meta
        );
    }

    @PostMapping
    @PreAuthorize("hasAnyRole('SUPER_ADMIN_ROLE')")
    public ResponseEntity<?> addUser(@RequestBody User user, HttpServletRequest request) {
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        userRepository.save(user);
        return new GlobalResponseHandler().handleResponse(
                "User created successfully",
                user,
                HttpStatus.OK,
                request
        );
    }

    @PutMapping("/{userId}")
    @PreAuthorize("hasAnyRole('SUPER_ADMIN_ROLE')")
    public ResponseEntity<?> updateUser(
            @PathVariable Long userId,
            @RequestBody User userData,
            HttpServletRequest request) {

        Optional<User> optionalUser = userRepository.findById(userId);

        if (optionalUser.isEmpty()) {
            return new GlobalResponseHandler().handleResponse(
                    "User id " + userId + " not found",
                    null,
                    HttpStatus.NOT_FOUND,
                    request
            );
        }

        User existingUser = optionalUser.get();

        existingUser.setEmail(userData.getEmail());
        existingUser.setName(userData.getName());
        existingUser.setLastname(userData.getLastname());

        userRepository.save(existingUser);

        return new GlobalResponseHandler().handleResponse(
                "User updated successfully",
                existingUser,
                HttpStatus.OK,
                request
        );
    }

    @PutMapping("/{userId}/activate")
    @PreAuthorize("hasAnyRole('SUPER_ADMIN_ROLE')")
    public ResponseEntity<?> activateUser(
            @PathVariable Long userId,
            HttpServletRequest request) {

        Optional<User> optionalUser = userRepository.findById(userId);

        if (optionalUser.isEmpty()) {
            return new GlobalResponseHandler().handleResponse(
                    "User id " + userId + " not found",
                    null,
                    HttpStatus.NOT_FOUND,
                    request
            );
        }

        User user = optionalUser.get();
        user.setActive(true);
        userRepository.save(user);

        return new GlobalResponseHandler().handleResponse(
                "Acción aplicada correctamente.",
                user,
                HttpStatus.OK,
                request
        );
    }

    @PutMapping("/{userId}/deactivate")
    @PreAuthorize("hasAnyRole('SUPER_ADMIN_ROLE')")
    public ResponseEntity<?> deactivateUser(
            @PathVariable Long userId,
            HttpServletRequest request) {

        Optional<User> optionalUser = userRepository.findById(userId);

        if (optionalUser.isEmpty()) {
            return new GlobalResponseHandler().handleResponse(
                    "User id " + userId + " not found",
                    null,
                    HttpStatus.NOT_FOUND,
                    request
            );
        }

        User user = optionalUser.get();
        user.setActive(false);
        userRepository.save(user);

        return new GlobalResponseHandler().handleResponse(
                "Acción aplicada correctamente.",
                user,
                HttpStatus.OK,
                request
        );
    }

    @DeleteMapping("/{userId}")
    @PreAuthorize("hasAnyRole('SUPER_ADMIN_ROLE')")
    public ResponseEntity<?> deleteUser(@PathVariable Long userId, HttpServletRequest request) {
        Optional<User> foundOrder = userRepository.findById(userId);

        if (foundOrder.isPresent()) {
            userRepository.deleteById(userId);
            return new GlobalResponseHandler().handleResponse(
                    "User deleted successfully",
                    foundOrder.get(),
                    HttpStatus.OK,
                    request
            );
        }

        return new GlobalResponseHandler().handleResponse(
                "User id " + userId + " not found",
                null,
                HttpStatus.NOT_FOUND,
                request
        );
    }

    @GetMapping("/me")
    @PreAuthorize("isAuthenticated()")
    public User authenticatedUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return (User) authentication.getPrincipal();
    }

    @GetMapping("/privacy")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<?> getMyPrivacySetting(HttpServletRequest request) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        User user = (User) auth.getPrincipal();

        return new GlobalResponseHandler().handleResponse(
                "Ok",
                user.getVisibility(),
                HttpStatus.OK,
                request
        );
    }

    @PutMapping("/privacy")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<?> updateMyPrivacySetting(
            @RequestBody PrivacyRequest request,
            HttpServletRequest httpRequest) {

        if (!"public".equals(request.getVisibility()) && !"private".equals(request.getVisibility())) {
            return new GlobalResponseHandler().handleResponse(
                    "Valor inválido. Solo se permite 'public' o 'private'.",
                    null,
                    HttpStatus.BAD_REQUEST,
                    httpRequest
            );
        }

        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        User user = (User) auth.getPrincipal();

        user.setVisibility(request.getVisibility());
        userRepository.save(user);

        return new GlobalResponseHandler().handleResponse(
                "Configuración actualizada correctamente.",
                user,
                HttpStatus.OK,
                httpRequest
        );
    }

    @GetMapping("/profile")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<?> getMyProfile(HttpServletRequest request) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        User user = (User) auth.getPrincipal();

        return new GlobalResponseHandler().handleResponse(
                "Perfil obtenido correctamente.",
                user,
                HttpStatus.OK,
                request
        );
    }

    @PutMapping("/profile")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<?> updateMyProfile(@RequestBody User updatedData, HttpServletRequest request) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        User principal = (User) auth.getPrincipal();

        User user = userRepository.findByEmail(principal.getEmail())
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

        if (updatedData.getName() != null && !updatedData.getName().isBlank()) {
            user.setName(updatedData.getName());
        }
        if (updatedData.getLastname() != null && !updatedData.getLastname().isBlank()) {
            user.setLastname(updatedData.getLastname());
        }
        if (updatedData.getEmail() != null && !updatedData.getEmail().isBlank()) {
            user.setEmail(updatedData.getEmail());
        }
        if (updatedData.getBio() != null) {
            user.setBio(updatedData.getBio());
        }
        if (updatedData.getVisibility() != null && !updatedData.getVisibility().isBlank()) {
            user.setVisibility(updatedData.getVisibility());
        }
        if (updatedData.getAvatarUrl() != null && !updatedData.getAvatarUrl().isBlank()) {
            user.setAvatarUrl(updatedData.getAvatarUrl());
        }
        if (updatedData.getPassword() != null && !updatedData.getPassword().isBlank()) {
            user.setPassword(passwordEncoder.encode(updatedData.getPassword()));
        }

        userRepository.save(user);

        return new GlobalResponseHandler().handleResponse(
                "Datos personales actualizados correctamente.",
                user,
                HttpStatus.OK,
                request
        );
    }

    public static class PrivacyRequest {
        private String visibility;

        public String getVisibility() {
            return visibility;
        }

        public void setVisibility(String visibility) {
            this.visibility = visibility;
        }
    }

    @GetMapping("/search")
    @PreAuthorize("hasAnyRole('SUPER_ADMIN_ROLE')")
    public ResponseEntity<?> searchUsers(
            @RequestParam(required = false) String name,
            @RequestParam(required = false) String email,
            @RequestParam(required = false) Boolean active,
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int size,
            HttpServletRequest request) {

        Pageable pageable = PageRequest.of(page - 1, size);
        Page<User> usersPage = userRepository.searchUsers(name, email, active, pageable);

        Meta meta = new Meta(request.getMethod(), request.getRequestURL().toString());
        meta.setTotalPages(usersPage.getTotalPages());
        meta.setTotalElements(usersPage.getTotalElements());
        meta.setPageNumber(usersPage.getNumber() + 1);
        meta.setPageSize(usersPage.getSize());

        return new GlobalResponseHandler().handleResponse(
                "Users retrieved successfully by filters",
                usersPage.getContent(),
                HttpStatus.OK,
                meta
        );
    }
}
