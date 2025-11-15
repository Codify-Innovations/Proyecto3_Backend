package com.project.demo.logic.entity.vehicle;

import com.project.demo.logic.entity.user.User;
import com.project.demo.logic.entity.user.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class VehicleCustomizationService {

    private final VehicleCustomizationRepository repository;
    private final UserRepository userRepository;

    public VehicleCustomizationService(VehicleCustomizationRepository repository, UserRepository userRepository) {
        this.repository = repository;
        this.userRepository = userRepository;
    }

    @Transactional
    public VehicleCustomization saveCustomization(Long userId, VehicleCustomization customization) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

        // Buscar si ya existe una configuración para este usuario
        VehicleCustomization existing = repository.findByUserId(userId).orElse(null);

        if (existing != null) {
            // Actualizar la configuración existente
            if (customization.getModelo() != null) existing.setModelo(customization.getModelo());
            if (customization.getCarroceria() != null) existing.setCarroceria(customization.getCarroceria());
            existing.setVidriosPolarizados(customization.isVidriosPolarizados());
            if (customization.getInterior() != null) existing.setInterior(customization.getInterior());
            if (customization.getRines() != null) existing.setRines(customization.getRines());
            if (customization.getLucesFront() != null) existing.setLucesFront(customization.getLucesFront());

            return repository.save(existing);
        } else {
            // Crear una nueva configuración
            customization.setUser(user);
            return repository.save(customization);
        }
    }

    @Transactional(readOnly = true)
    public VehicleCustomization getCustomizationByUser(Long userId) {
        return repository.findByUserId(userId)
                .orElseThrow(() -> new RuntimeException("No se encontró configuración para este usuario"));
    }
}
