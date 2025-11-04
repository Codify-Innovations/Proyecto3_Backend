package com.project.demo.logic.entity.user;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class UserService {

    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    // =====================================================
    // 🔒 CONFIGURACIÓN DE PRIVACIDAD (ya existente)
    // =====================================================
    @Transactional(readOnly = true)
    public String getPrivacySetting(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));
        return user.getVisibility();
    }

    @Transactional
    public void updatePrivacySetting(Long userId, String visibility) {
        if (!visibility.equals("public") && !visibility.equals("private")) {
            throw new IllegalArgumentException("Valor inválido. Solo se permite público o privado.");
        }

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

        user.setVisibility(visibility);
        userRepository.save(user);
    }

    // =====================================================
    // 👤 ACTUALIZACIÓN DE DATOS PERSONALES (ajustado)
    // =====================================================
    @Transactional
    public User updateUserProfile(Long userId, User updatedData) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

        // ✅ Cambiar a getName() / setName() y getLastname() / setLastname()
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
            user.setPassword(updatedData.getPassword());
        }

        return userRepository.save(user);
    }

    @Transactional(readOnly = true)
    public User getUserProfile(Long userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));
    }
}