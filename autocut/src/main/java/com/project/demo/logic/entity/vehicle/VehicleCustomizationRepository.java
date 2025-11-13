package com.project.demo.logic.entity.vehicle;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface VehicleCustomizationRepository extends JpaRepository<VehicleCustomization, Long> {
    Optional<VehicleCustomization> findByUserId(Long userId);
}
