package com.project.demo.rest.user;

import com.project.demo.logic.entity.http.GlobalResponseHandler;
import com.project.demo.logic.entity.http.Meta;
import com.project.demo.logic.entity.services.logros.LogrosService;
import com.project.demo.logic.entity.user.User;
import com.project.demo.logic.entity.user.UserRepository;
import com.project.demo.logic.entity.vehicle.VehicleCustomization;
import com.project.demo.logic.entity.vehicle.VehicleCustomizationService;
import com.project.demo.logic.entity.vehiculo.VehiculoRepository;

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

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/users")
public class UserRestController {

    @Autowired private UserRepository userRepository;
    @Autowired private PasswordEncoder passwordEncoder;
    @Autowired private VehicleCustomizationService customizationService;
    @Autowired private VehiculoRepository vehiculoRepository;
    @Autowired private LogrosService logrosService;

    
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
            @RequestBody User data,
            HttpServletRequest request) {

        Optional<User> optional = userRepository.findById(userId);

        if (optional.isEmpty()) {
            return new GlobalResponseHandler().handleResponse(
                    "User id " + userId + " not found",
                    null,
                    HttpStatus.NOT_FOUND,
                    request
            );
        }

        User user = optional.get();

        user.setEmail(data.getEmail());
        user.setName(data.getName());
        user.setLastname(data.getLastname());

        userRepository.save(user);

        return new GlobalResponseHandler().handleResponse(
                "User updated successfully",
                user,
                HttpStatus.OK,
                request
        );
    }

    
    @PutMapping("/{userId}/activate")
    @PreAuthorize("hasAnyRole('SUPER_ADMIN_ROLE')")
    public ResponseEntity<?> activateUser(@PathVariable Long userId, HttpServletRequest request) {

        Optional<User> optional = userRepository.findById(userId);

        if (optional.isEmpty()) {
            return new GlobalResponseHandler().handleResponse(
                    "User id " + userId + " not found",
                    null,
                    HttpStatus.NOT_FOUND,
                    request
            );
        }

        User user = optional.get();
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
    public ResponseEntity<?> deactivateUser(@PathVariable Long userId, HttpServletRequest request) {

        Optional<User> optional = userRepository.findById(userId);

        if (optional.isEmpty()) {
            return new GlobalResponseHandler().handleResponse(
                    "User id " + userId + " not found",
                    null,
                    HttpStatus.NOT_FOUND,
                    request
            );
        }

        User user = optional.get();
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

        Optional<User> optional = userRepository.findById(userId);

        if (optional.isPresent()) {
            userRepository.deleteById(userId);
            return new GlobalResponseHandler().handleResponse(
                    "User deleted successfully",
                    optional.get(),
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
        return (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
    }

   
    public static class PrivacyRequest {
        private String visibility;
        public String getVisibility() { return visibility; }
        public void setVisibility(String visibility) { this.visibility = visibility; }
    }

    @GetMapping("/privacy")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<?> getMyPrivacy(HttpServletRequest req) {
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        return new GlobalResponseHandler().handleResponse("Ok", user.getVisibility(), HttpStatus.OK, req);
    }

    @PutMapping("/privacy")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<?> updateMyPrivacy(
            @RequestBody PrivacyRequest body,
            HttpServletRequest req) {

        if (!"public".equals(body.getVisibility()) && !"private".equals(body.getVisibility())) {
            return new GlobalResponseHandler().handleResponse(
                    "Valor inválido",
                    null,
                    HttpStatus.BAD_REQUEST,
                    req
            );
        }

        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        user.setVisibility(body.getVisibility());
        userRepository.save(user);

        return new GlobalResponseHandler().handleResponse(
                "Configuración actualizada correctamente.",
                user,
                HttpStatus.OK,
                req
        );
    }

    
    @GetMapping("/profile")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<?> getMyProfile(HttpServletRequest request) {
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        return new GlobalResponseHandler().handleResponse("Perfil obtenido correctamente.", user, HttpStatus.OK, request);
    }

    @PutMapping("/profile")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<?> updateMyProfile(@RequestBody User data, HttpServletRequest request) {

        User principal = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        User user = userRepository.findByEmail(principal.getEmail())
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

        if (data.getName() != null) user.setName(data.getName());
        if (data.getLastname() != null) user.setLastname(data.getLastname());
        if (data.getEmail() != null) user.setEmail(data.getEmail());
        if (data.getBio() != null) user.setBio(data.getBio());
        if (data.getVisibility() != null) user.setVisibility(data.getVisibility());
        if (data.getAvatarUrl() != null) user.setAvatarUrl(data.getAvatarUrl());
        if (data.getPassword() != null)
            user.setPassword(passwordEncoder.encode(data.getPassword()));

        userRepository.save(user);

        return new GlobalResponseHandler().handleResponse(
                "Datos personales actualizados correctamente.",
                user,
                HttpStatus.OK,
                request
        );
    }

    
    @GetMapping("/explore-users")
    public ResponseEntity<?> getPublicUsers(HttpServletRequest request) {

        List<User> users = userRepository.findAll();

        var mapped = users.stream().map(u -> {

            Map<String, Object> customizationMap = null;

            try {
                VehicleCustomization c = customizationService.getCustomizationByUser(u.getId());

                if (c != null) {
                    customizationMap = new HashMap<>();
                    customizationMap.put("id", c.getId());
                    customizationMap.put("modelo", c.getModelo());
                    customizationMap.put("carroceria", c.getCarroceria());
                    customizationMap.put("interior", c.getInterior());
                    customizationMap.put("lucesFront", c.getLucesFront());
                    customizationMap.put("rines", c.getRines());
                    customizationMap.put("fechaCreacion", c.getFechaCreacion());
                    customizationMap.put("fechaActualizacion", c.getFechaActualizacion());
                }

            } catch (Exception ignored) {}

            return new PublicUserResponse(
                    u.getUsername(),
                    u.getAvatarUrl(),
                    u.getVisibility(),
                    (u.getCreatedAt() != null ? u.getCreatedAt().toString() : null),
                    customizationMap
            );
        }).toList();

        return new GlobalResponseHandler().handleResponse(
                "Usuarios encontrados",
                mapped,
                HttpStatus.OK,
                request
        );
    }

    @GetMapping("/explore-users/{valor}")
public ResponseEntity<?> getPublicProfile(
        @PathVariable String valor,
        HttpServletRequest request
) {


    Optional<User> optional = userRepository.findByUsername(valor);

    if (optional.isEmpty() && valor.contains("@")) {
        optional = userRepository.findByEmail(valor);
    }

    if (optional.isEmpty()) {
        return new GlobalResponseHandler().handleResponse(
                "Usuario no encontrado",
                HttpStatus.NOT_FOUND,
                request
        );
    }

    User user = optional.get();

    if ("private".equalsIgnoreCase(user.getVisibility())) {
        return new GlobalResponseHandler().handleResponse(
                "Perfil privado",
                new PublicProfileResponse(false, null),
                HttpStatus.OK,
                request
        );
    }

    Object customization = null;
    try {
        customization = customizationService.getCustomizationByUser(user.getId());
    } catch (Exception ignored) {}

    var cars = vehiculoRepository.findByUsuarioId(user.getId());

    var logros = logrosService.getUnlockedAchievements(user);

    var fullProfile = new FullPublicUserProfile(
            user.getUsername(),
            user.getName(),
            user.getLastname(),
            user.getEmail(),
            user.getAvatarUrl(),
            user.getVisibility(),
            user.getBio(),
            user.getCreatedAt() != null ? user.getCreatedAt().toString() : null,
            customization,
            cars,
            logros
    );

    return new GlobalResponseHandler().handleResponse(
            "Perfil público cargado correctamente",
            new PublicProfileResponse(true, fullProfile),
            HttpStatus.OK,
            request
    );
}


   
    public static class PublicUserResponse {
        public String username;
        public String avatarUrl;
        public String visibility;
        public String createdAt;
        public Object customization;
        public PublicUserResponse(String username, String avatarUrl, String visibility, String createdAt, Object customization) {
            this.username = username;
            this.avatarUrl = avatarUrl;
            this.visibility = visibility;
            this.createdAt = createdAt;
            this.customization = customization;
        }
    }

    public static class FullPublicUserProfile {
        public String username, name, lastname, email, avatarUrl, visibility, bio, createdAt;
        public Object customization, cars, logros;

        public FullPublicUserProfile(String username, String name, String lastname, String email,
                                     String avatarUrl, String visibility, String bio, String createdAt,
                                     Object customization, Object cars, Object logros) {
            this.username = username;
            this.name = name;
            this.lastname = lastname;
            this.email = email;
            this.avatarUrl = avatarUrl;
            this.visibility = visibility;
            this.bio = bio;
            this.createdAt = createdAt;
            this.customization = customization;
            this.cars = cars;
            this.logros = logros;
        }
    }

    public static class PublicProfileResponse {
        public boolean allowed;
        public Object profile;

        public PublicProfileResponse(boolean allowed, Object profile) {
            this.allowed = allowed;
            this.profile = profile;
        }
    }
    
}
