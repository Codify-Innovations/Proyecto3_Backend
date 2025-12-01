package com.project.demo.rest.metricas;

import com.project.demo.logic.entity.http.GlobalResponseHandler;
import com.project.demo.logic.entity.http.Meta;
import com.project.demo.logic.entity.services.metricas.MetricasService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;

@RestController
@RequestMapping("/api/metricas")
public class MetricasRestController {

    @Autowired
    private MetricasService metricasService;

    // Summary general
    @GetMapping("/user/{userId}/summary")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<?> getSummary(
            @PathVariable Long userId,
            HttpServletRequest request
    ) {
        var result = metricasService.getSummary(userId);

        return new GlobalResponseHandler().handleResponse(
                "Resumen de métricas obtenido correctamente.",
                result,
                HttpStatus.OK,
                request
        );
    }

    // Metricas agrupadas por fecha
    @GetMapping("/user/{userId}/by-date")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<?> getMetricsByDateRange(
            @PathVariable Long userId,
            @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate start,
            @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate end,
            HttpServletRequest request
    ) {
        if (end.isBefore(start)) {
            return new GlobalResponseHandler().handleResponse(
                    "La fecha final no puede ser menor que la fecha inicial.",
                    HttpStatus.BAD_REQUEST,
                    request
            );
        }

        var result = metricasService.getMetricsByDateRange(userId, start, end);

        return new GlobalResponseHandler().handleResponse(
                "Métricas por rango de fechas obtenidas correctamente.",
                result,
                HttpStatus.OK,
                request
        );
    }
}
