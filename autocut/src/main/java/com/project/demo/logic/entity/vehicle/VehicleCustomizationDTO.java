package com.project.demo.logic.entity.vehicle;

import java.util.Date;

public record VehicleCustomizationDTO(
        Long id,
        String modelo,
        String carroceria,
        boolean vidriosPolarizados,
        String interior,
        String rines,
        String lucesFront,
        Date fechaCreacion,
        Date fechaActualizacion
) {}
