package com.project.demo.logic.entity.services.reporte.generadorReportes;

import org.springframework.stereotype.Component;
import java.nio.charset.StandardCharsets;

@Component
public class GeneradorCSV {

    public byte[] generarCSV(
            long videos,
            long vehiculos,
            long analisis,
            long logros,
            long nuevosUsuarios,
            long activos,
            long inactivos,
            String fechaInicio,
            String fechaFin,
            boolean esGlobal,
            String fechaGenerado
    ) {

        StringBuilder sb = new StringBuilder();

        // ========= ENCABEZADO PROFESIONAL =========
        sb.append("Reporte Administrativo AutoCut\n");
        sb.append("Generado el;").append("\"").append(fechaGenerado).append("\"\n");

        if (!esGlobal) {
            sb.append("Rango Seleccionado;\n");
            sb.append("Fecha Inicio;").append("\"").append(fechaInicio).append("\"\n");
            sb.append("Fecha Fin;").append("\"").append(fechaFin).append("\"\n");
        }

        sb.append("\n"); // Espacio entre metadata y tabla

        // ========= TABLA DE MÉTRICAS =========
        sb.append("Métrica;Valor\n");
        sb.append("\"Total Videos Generados\";").append(videos).append("\n");
        sb.append("\"Total Vehículos Detectados\";").append(vehiculos).append("\n");
        sb.append("\"Total Análisis IA\";").append(analisis).append("\n");
        sb.append("\"Total Logros Obtenidos\";").append(logros).append("\n");
        sb.append("\"Nuevos Usuarios\";").append(nuevosUsuarios).append("\n");
        sb.append("\"Usuarios Activos\";").append(activos).append("\n");
        sb.append("\"Usuarios Inactivos\";").append(inactivos).append("\n");

        byte[] bom = new byte[] {(byte)0xEF, (byte)0xBB, (byte)0xBF};

        byte[] csvBytes = sb.toString().getBytes(StandardCharsets.UTF_8);

        byte[] finalBytes = new byte[bom.length + csvBytes.length];
        System.arraycopy(bom, 0, finalBytes, 0, bom.length);
        System.arraycopy(csvBytes, 0, finalBytes, bom.length, csvBytes.length);

        return finalBytes;
    }
}
