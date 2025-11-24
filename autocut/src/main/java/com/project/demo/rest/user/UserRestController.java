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
import java.util.Date;



import java.util.Optional;

@RestController
@RequestMapping("/users")
public class UserRestController {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;
  

@Autowired
private VehicleCustomizationService customizationService;

@Autowired
private VehiculoRepository vehiculoRepository;

@Autowired
private LogrosService logrosService;



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

@GetMapping("/explore-users")
public ResponseEntity<?> getPublicUsers(HttpServletRequest request) {

    var users = userRepository.findAll();

    var mapped = users.stream()
            .map(u -> {

                Object customization = null;
                try {
                    customization = customizationService.getCustomizationByUser(u.getId());
                } catch (Exception ignored) {}

                return new PublicUserResponse(
                        u.getUsername(),                   
                        u.getAvatarUrl(),
                        u.getVisibility(),
                        u.getCreatedAt() != null ? u.getCreatedAt().toString() : null,
                        customization
                );
            })
            .toList();

    return new GlobalResponseHandler().handleResponse(
            "Usuarios encontrados",
            mapped,
            HttpStatus.OK,
            request
    );
}


@GetMapping("/explore-users/{username}")
public ResponseEntity<?> getPublicProfile(
        @PathVariable String username,
        HttpServletRequest request
) {

    Optional<User> optional = userRepository.findByUsername(username);

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


    var cars = vehiculoRepository.findByUsuarioId(user.getId().intValue());

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

    public PublicUserResponse(
            String username,
            String avatarUrl,
            String visibility,
            String createdAt,
            Object customization
    ) {
        this.username = username;
        this.avatarUrl = avatarUrl;
        this.visibility = visibility;
        this.createdAt = createdAt;
        this.customization = customization;
    }
}


public static class FullPublicUserProfile {

    public String username;       
    public String name;
    public String lastname;
    public String email;
    public String avatarUrl;
    public String visibility;
    public String bio;
    public String createdAt;

    public Object customization;
    public Object cars;
    public Object logros;

    public FullPublicUserProfile(
            String username,      
            String name,
            String lastname,
            String email,
            String avatarUrl,
            String visibility,
            String bio,
            String createdAt,
            Object customization,
            Object cars,
            Object logros
    ) {
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