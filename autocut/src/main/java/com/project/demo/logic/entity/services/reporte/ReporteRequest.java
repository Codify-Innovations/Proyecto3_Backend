package com.project.demo.logic.entity.services.reporte;

import java.time.LocalDate;

public record ReporteRequest(
        boolean global,
        LocalDate startDate,
        LocalDate endDate,
        String format,
        Long adminUserId
) {}