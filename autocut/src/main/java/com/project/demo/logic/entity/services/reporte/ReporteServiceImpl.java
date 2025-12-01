package com.project.demo.logic.entity.services.reporte;

import com.project.demo.logic.entity.reporte.ReporteHistorial;
import com.project.demo.logic.entity.reporte.ReporteHistorialRepository;
import com.project.demo.logic.entity.services.metricas.AdminMetricasDTO;
import com.project.demo.logic.entity.services.metricas.MetricasService;
import com.project.demo.logic.entity.services.reporte.generadorReportes.GeneradorPDF;
import com.project.demo.logic.entity.user.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class ReporteServiceImpl implements ReporteService{


    @Autowired
    private MetricasService metricasService;

    @Autowired
    private ReporteHistorialRepository historialRepository;

    @Override
    public AdminMetricasDTO generarReporte(ReporteRequest request) {

        if (request.global()) {
            return metricasService.getAdminMetricsGlobal();
        }

        if (request.startDate() == null || request.endDate() == null) {
            throw new IllegalArgumentException("Las fechas de inicio y fin son obligatorias para reportes por rango.");
        }

        return metricasService.getAdminMetrics(
                request.startDate(),
                request.endDate()
        );
    }

    @Override
    public ReporteHistorial guardarHistorial(User usuario, ReporteRequest request,
                                             long tamanioBytes,
                                             String nombreArchivo) {

        ReporteHistorial historial = new ReporteHistorial();
        historial.setUsuario(usuario);
        historial.setTipo(request.format());
        historial.setEsGlobal(request.global());
        historial.setFechaInicio(request.startDate());
        historial.setFechaFin(request.endDate());
        historial.setFechaGenerado(LocalDateTime.now());
        historial.setTamanioBytes(tamanioBytes);
        historial.setNombreArchivo(nombreArchivo);

        return historialRepository.save(historial);
    }
}
