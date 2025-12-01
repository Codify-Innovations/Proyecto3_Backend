package com.project.demo.logic.entity.services.reporteHistorial;

import java.time.LocalDateTime;
import java.time.LocalDate;

public record ReporteHistorialDTO(
        Long id,
        String nombreArchivo,
        String tipo,
        boolean esGlobal,
        LocalDate fechaInicio,
        LocalDate fechaFin,
        LocalDateTime fechaGenerado,
        long tamanioBytes
) {}