package com.project.demo.logic.entity.services.metricas;

public record AdminMetricasDTO(
        long totalVideos,
        long totalVehiculos,
        long totalAnalisis,
        long totalLogros,
        long nuevosUsuarios,
        long usuariosActivos,
        long usuariosInactivos
) {
}

