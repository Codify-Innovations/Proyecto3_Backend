package com.project.demo.logic.entity.services.reporteHistorial;

import com.project.demo.logic.entity.reporte.ReporteHistorialRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ReporteHistorialServiceImpl implements ReporteHistorialService {

    @Autowired
    private ReporteHistorialRepository historialRepository;

    @Override
    public List<ReporteHistorialDTO> obtenerHistorial() {

        return historialRepository.findAll()
                .stream()
                .map(r -> new ReporteHistorialDTO(
                        r.getId(),
                        r.getNombreArchivo(),
                        r.getTipo(),
                        r.isEsGlobal(),
                        r.getFechaInicio(),
                        r.getFechaFin(),
                        r.getFechaGenerado(),
                        r.getTamanioBytes()
                ))
                .toList();
    }}
