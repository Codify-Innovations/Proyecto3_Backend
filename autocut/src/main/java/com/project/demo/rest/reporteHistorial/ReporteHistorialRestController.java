package com.project.demo.rest.reporteHistorial;

import com.project.demo.logic.entity.http.GlobalResponseHandler;
import com.project.demo.logic.entity.services.reporteHistorial.ReporteHistorialService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/reportes/historial")
public class ReporteHistorialRestController {

    @Autowired
    private ReporteHistorialService historialService;

    @GetMapping
    @PreAuthorize("hasRole('SUPER_ADMIN_ROLE')")
    public ResponseEntity<?> obtenerHistorial(HttpServletRequest request) {

        var historial = historialService.obtenerHistorial();

        return new GlobalResponseHandler().handleResponse(
                "Historial de reportes obtenido correctamente.",
                historial,
                HttpStatus.OK,
                request
        );
    }
}
