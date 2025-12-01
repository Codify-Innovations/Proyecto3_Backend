package com.project.demo.rest.reporte;

import com.project.demo.logic.entity.services.metricas.AdminMetricasDTO;
import com.project.demo.logic.entity.services.reporte.ReporteRequest;
import com.project.demo.logic.entity.services.reporte.ReporteService;
import com.project.demo.logic.entity.services.reporte.generadorReportes.GeneradorCSV;
import com.project.demo.logic.entity.services.reporte.generadorReportes.GeneradorPDF;
import com.project.demo.logic.entity.user.User;
import com.project.demo.logic.entity.user.UserRepository;
import com.project.demo.logic.entity.user.UserService;
import com.project.demo.logic.utils.TemplateRenderer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

@RestController
@RequestMapping("/api/reportes")
public class ReporteRestController {

    @Autowired
    private ReporteService reporteService;

    @Autowired
    private TemplateRenderer templateRenderer;

    @Autowired
    private GeneradorPDF generadorPDF;

    @Autowired
    GeneradorCSV generadorCSV;

    @Autowired
    UserRepository userRepository;

    @PostMapping("/generate/{usuarioId}")
    @PreAuthorize("hasRole('SUPER_ADMIN_ROLE')")
    public ResponseEntity<byte[]> generarReporte(@PathVariable("usuarioId") Long usuarioId, @RequestBody ReporteRequest request) {

        User usuario = userRepository.findById(usuarioId)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado."));

        AdminMetricasDTO metricas = reporteService.generarReporte(request);

        if (metricas == null) {
            return ResponseEntity.badRequest().build();
        }

        // FORMATTERS PARA FECHAS
        DateTimeFormatter fechaCompletaFormatter =
                DateTimeFormatter.ofPattern("dd 'de' MMMM, yyyy — HH:mm")
                        .withLocale(new Locale("es", "ES"));

        DateTimeFormatter fechaSimpleFormatter =
                DateTimeFormatter.ofPattern("dd 'de' MMMM, yyyy")
                        .withLocale(new Locale("es", "ES"));

        String fechaFormateada = LocalDateTime.now().format(fechaCompletaFormatter);

        String fechaInicio = null;
        String fechaFin = null;

        if (!request.global()) {
            if (request.startDate() != null) {
                fechaInicio = request.startDate().format(fechaSimpleFormatter);
            }

            if (request.endDate() != null) {
                fechaFin = request.endDate().format(fechaSimpleFormatter);
            }
        }

        byte[] archivoGenerado;
        String nombreArchivo;

        // ===== GENERAR PDF ===== //
        if (request.format().equalsIgnoreCase("PDF")) {

            Map<String, Object> data = new HashMap<>();

            data.put("fecha", fechaFormateada);
            data.put("videos", metricas.totalVideos());
            data.put("vehiculos", metricas.totalVehiculos());
            data.put("analisis", metricas.totalAnalisis());
            data.put("logros", metricas.totalLogros());
            data.put("nuevosUsuarios", metricas.nuevosUsuarios());
            data.put("activos", metricas.usuariosActivos());
            data.put("inactivos", metricas.usuariosInactivos());

            if (fechaInicio != null) data.put("fechaInicio", fechaInicio);
            if (fechaFin != null) data.put("fechaFin", fechaFin);

            String html = templateRenderer.render("reporte-admin", data);

            archivoGenerado= generadorPDF.generarDesdeHtml(html);

            nombreArchivo = "reporte-" + System.currentTimeMillis() + ".pdf";

            reporteService.guardarHistorial(usuario, request, archivoGenerado.length, nombreArchivo);

            return ResponseEntity.ok()
                    .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=" + nombreArchivo)
                    .contentType(MediaType.APPLICATION_PDF)
                    .body(archivoGenerado);
        }

        // ===== GENERAR CSV ===== //
        else if (request.format().equalsIgnoreCase("CSV")) {

            archivoGenerado = generadorCSV.generarCSV(
                    metricas.totalVideos(),
                    metricas.totalVehiculos(),
                    metricas.totalAnalisis(),
                    metricas.totalLogros(),
                    metricas.nuevosUsuarios(),
                    metricas.usuariosActivos(),
                    metricas.usuariosInactivos(),
                    fechaInicio,
                    fechaFin,
                    request.global(),
                    fechaFormateada
            );

            nombreArchivo = "reporte-" + System.currentTimeMillis() + ".csv";

            reporteService.guardarHistorial(usuario, request, archivoGenerado.length, nombreArchivo);

            return ResponseEntity.ok()
                    .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=" + nombreArchivo)
                    .contentType(MediaType.TEXT_PLAIN)
                    .body(archivoGenerado);
        }

        return ResponseEntity.badRequest().body(null);
    }
}