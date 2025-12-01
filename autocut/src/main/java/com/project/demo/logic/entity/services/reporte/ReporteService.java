package com.project.demo.logic.entity.services.reporte;

import com.project.demo.logic.entity.reporte.ReporteHistorial;
import com.project.demo.logic.entity.services.metricas.AdminMetricasDTO;
import com.project.demo.logic.entity.user.User;

public interface ReporteService {

    AdminMetricasDTO generarReporte(ReporteRequest request);

    ReporteHistorial guardarHistorial(User usuario, ReporteRequest request, long tamanioBytes, String nombreArchivo);
}