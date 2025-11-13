package com.project.demo.logic.entity.vehicle;

public class VehicleCustomizationMapper {
    public static VehicleCustomizationDTO toDto(VehicleCustomization entity) {
        if (entity == null) return null;
        return new VehicleCustomizationDTO(
                entity.getId(),
                entity.getModelo(),
                entity.getCarroceria(),
                entity.isVidriosPolarizados(),
                entity.getInterior(),
                entity.getRines(),
                entity.getLucesFront(),
                entity.getFechaCreacion(),
                entity.getFechaActualizacion()
        );
    }
}
