package com.project.demo.logic.entity.services.contenidoAnalizadoIA;

public record ContenidoAnalizadoIADTO(
        Long userId,
        String sourceUrl,
        String analysisType,
        Double score
) {}