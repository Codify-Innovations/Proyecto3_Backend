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

    // ========================================
    // 🔹 ENDPOINTS EXISTENTES (NO TOCAR)
    // ========================================

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

        return new GlobalResponseHandler().handleResponse("Users retrieved successfully",
                usersPage.getContent(), HttpStatus.OK, meta);
    }

    @PostMapping
    @PreAuthorize("hasAnyRole('SUPER_ADMIN_ROLE')")
    public ResponseEntity<?> addUser(@RequestBody User user, HttpServletRequest request) {
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        userRepository.save(user);
        return new GlobalResponseHandler().handleResponse("User updated successfully",
                user, HttpStatus.OK, request);
    }

    @PutMapping("/{userId}")
    @PreAuthorize("hasAnyRole('SUPER_ADMIN_ROLE')")
    public ResponseEntity<?> updateUser(@PathVariable Long userId, @RequestBody User user, HttpServletRequest request) {
        Optional<User> foundOrder = userRepository.findById(userId);
        if (foundOrder.isPresent()) {
            user.setPassword(passwordEncoder.encode(user.getPassword()));
            userRepository.save(user);
            return new GlobalResponseHandler().handleResponse("User updated successfully",
                    user, HttpStatus.OK, request);
        } else {
            return new GlobalResponseHandler().handleResponse("User id " + userId + " not found",
                    HttpStatus.NOT_FOUND, request);
        }
    }

    @DeleteMapping("/{userId}")
    @PreAuthorize("hasAnyRole('SUPER_ADMIN_ROLE')")
    public ResponseEntity<?> deleteUser(@PathVariable Long userId, HttpServletRequest request) {
        Optional<User> foundOrder = userRepository.findById(userId);
        if (foundOrder.isPresent()) {
            userRepository.deleteById(userId);
            return new GlobalResponseHandler().handleResponse("User deleted successfully",
                    foundOrder.get(), HttpStatus.OK, request);
        } else {
            return new GlobalResponseHandler().handleResponse("User id " + userId + " not found",
                    HttpStatus.NOT_FOUND, request);
        }
    }

    @GetMapping("/me")
    @PreAuthorize("isAuthenticated()")
    public User authenticatedUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return (User) authentication.getPrincipal();
    }

    // ========================================
    // 🆕 NUEVOS ENDPOINTS DE CONFIGURACIÓN DE PRIVACIDAD
    // ========================================

    @GetMapping("/privacy")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<?> getMyPrivacySetting(HttpServletRequest request) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        User user = (User) auth.getPrincipal();

        return new GlobalResponseHandler().handleResponse(
                "Configuración de privacidad obtenida correctamente.",
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

    // ========================================
    // 🆕 NUEVOS ENDPOINTS DE PERFIL PERSONAL
    // ========================================

    /**
     * GET /users/profile → Obtiene el perfil del usuario autenticado
     */
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

    /**
     * PUT /users/profile → Actualiza los datos personales del usuario autenticado
     */
    @PutMapping("/profile")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<?> updateMyProfile(@RequestBody User updatedData, HttpServletRequest request) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        User user = (User) auth.getPrincipal();

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

    // DTO interno para visibilidad
    public static class PrivacyRequest {
        private String visibility;

        public String getVisibility() {
            return visibility;
        }

        public void setVisibility(String visibility) {
            this.visibility = visibility;
        }
    }
}