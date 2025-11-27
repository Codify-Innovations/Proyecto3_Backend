package com.project.demo.logic.entity.services.metricas;
import com.project.demo.logic.entity.contenidoAnalizadoIA.ContenidoAnalizadoIARepository;
import com.project.demo.logic.entity.usuarioLogro.UsuarioLogro;
import com.project.demo.logic.entity.usuarioLogro.UsuarioLogroRepository;
import com.project.demo.logic.entity.vehiculo.VehiculoRepository;
import com.project.demo.logic.entity.videoGeneradoIA.VideoGeneradoIARepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class MetricasService {

    @Autowired
    private VehiculoRepository vehiculoRepository;

    @Autowired
    private VideoGeneradoIARepository videoGeneradoIARepository;

    @Autowired
    private ContenidoAnalizadoIARepository contenidoAnalizadoIARepository;

    @Autowired
    private UsuarioLogroRepository usuarioLogroRepository;

    // ===== SUMMARY GENERAL ===== //
    public Map<String, Object> getSummary(Long userId) {
        long totalVehiculos = vehiculoRepository.countByUsuarioId(userId);
        long totalVideos = videoGeneradoIARepository.countByUsuarioId(userId);
        long totalAnalisis = contenidoAnalizadoIARepository.countByUsuarioId(userId);
        long totalLogros = usuarioLogroRepository.countByUsuarioId(userId);

        Double promedioScore = contenidoAnalizadoIARepository.averageScoreByUsuarioId(userId);

        return Map.of(
                "totalVehiculos", totalVehiculos,
                "totalVideosGenerados", totalVideos,
                "totalContenidoAnalizado", totalAnalisis,
                "totalLogrosDesbloqueados", totalLogros,
                "promedioScoreAnalisis", promedioScore != null ? promedioScore : 0.0
        );
    }

    // ===== MÉTRICAS AGRUPADAS POR FECHA ===== //
    public Map<String, Object> getMetricsByDateRange(Long userId, LocalDate start, LocalDate end) {

        Map<LocalDate, Long>  vehiculos = vehiculoRepository
                .findByUsuarioIdAndCreatedAtBetween(userId, start.atStartOfDay(), end.plusDays(1).atStartOfDay())
                .stream()
                .collect(Collectors.groupingBy(
                        v -> v.getCreatedAt().toLocalDate(),
                        Collectors.counting()
                ));

        Map<LocalDate, Long>  videos = videoGeneradoIARepository
                .findByUsuarioIdAndCreatedAtBetween(userId, start.atStartOfDay(), end.plusDays(1).atStartOfDay())
                .stream()
                .collect(Collectors.groupingBy(
                        v -> v.getCreatedAt().toLocalDate(),
                        Collectors.counting()
                ));

        Map<LocalDate, Long>  analisis = contenidoAnalizadoIARepository
                .findByUsuarioIdAndCreatedAtBetween(userId, start.atStartOfDay(), end.plusDays(1).atStartOfDay())
                .stream()
                .collect(Collectors.groupingBy(
                        a -> a.getCreatedAt().toLocalDate(),
                        Collectors.counting()
                ));

        Map<LocalDate, Long> logros = usuarioLogroRepository
                .findByUsuarioIdAndFechaDesbloqueoBetween(userId,
                        start.atStartOfDay(),
                        end.plusDays(1).atStartOfDay())
                .stream()
                .collect(Collectors.groupingBy(
                        l -> l.getFechaDesbloqueo().toLocalDate(),
                        Collectors.counting()
                ));


        return Map.of(
                "vehiculos", vehiculos,
                "videos", videos,
                "analisis", analisis,
                "logros", logros
        );
    }
}