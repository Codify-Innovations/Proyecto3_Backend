package com.project.demo.logic.entity.services.metricas;

import com.project.demo.logic.entity.contenidoAnalizadoIA.ContenidoAnalizadoIARepository;
import com.project.demo.logic.entity.user.UserRepository;
import com.project.demo.logic.entity.usuarioLogro.UsuarioLogroRepository;
import com.project.demo.logic.entity.vehiculo.VehiculoRepository;
import com.project.demo.logic.entity.videoGeneradoIA.VideoGeneradoIARepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class MetricasServiceImpl implements MetricasService {

    @Autowired
    private VehiculoRepository vehiculoRepository;

    @Autowired
    private VideoGeneradoIARepository videoGeneradoIARepository;

    @Autowired
    private ContenidoAnalizadoIARepository contenidoAnalizadoIARepository;

    @Autowired
    private UsuarioLogroRepository usuarioLogroRepository;

    @Autowired
    private UserRepository userRepository;


    // ===== SUMMARY GENERAL ===== //
    @Override
    public Map<String, Object> getSummary(Long userId) {
        long totalVehiculos = vehiculoRepository.countByUsuarioId(userId);
        long totalVideos = videoGeneradoIARepository.countByUsuarioId(userId);
        long totalAnalisis = contenidoAnalizadoIARepository.countByUsuarioId(userId);
        long totalLogros = usuarioLogroRepository.countByUsuarioId(userId);

        Double promedioScore = contenidoAnalizadoIARepository.averageScoreByUsuarioId(userId);

        return Map.of("totalVehiculos", totalVehiculos, "totalVideosGenerados", totalVideos, "totalContenidoAnalizado", totalAnalisis, "totalLogrosDesbloqueados", totalLogros, "promedioScoreAnalisis", promedioScore != null ? promedioScore : 0.0);
    }

    // ===== MÉTRICAS AGRUPADAS POR FECHA ===== //
    @Override
    public Map<String, Object> getMetricsByDateRange(Long userId, LocalDate start, LocalDate end) {

        Map<LocalDate, Long> vehiculos = vehiculoRepository.findByUsuarioIdAndCreatedAtBetween(userId, start.atStartOfDay(), end.plusDays(1).atStartOfDay()).stream().collect(Collectors.groupingBy(v -> v.getCreatedAt().toLocalDate(), Collectors.counting()));

        Map<LocalDate, Long> videos = videoGeneradoIARepository.findByUsuarioIdAndCreatedAtBetween(userId, start.atStartOfDay(), end.plusDays(1).atStartOfDay()).stream().collect(Collectors.groupingBy(v -> v.getCreatedAt().toLocalDate(), Collectors.counting()));

        Map<LocalDate, Long> analisis = contenidoAnalizadoIARepository.findByUsuarioIdAndCreatedAtBetween(userId, start.atStartOfDay(), end.plusDays(1).atStartOfDay()).stream().collect(Collectors.groupingBy(a -> a.getCreatedAt().toLocalDate(), Collectors.counting()));

        Map<LocalDate, Long> logros = usuarioLogroRepository.findByUsuarioIdAndFechaDesbloqueoBetween(userId, start.atStartOfDay(), end.plusDays(1).atStartOfDay()).stream().collect(Collectors.groupingBy(l -> l.getFechaDesbloqueo().toLocalDate(), Collectors.counting()));


        return Map.of("vehiculos", vehiculos, "videos", videos, "analisis", analisis, "logros", logros);
    }

    @Override
    public AdminMetricasDTO getAdminMetrics(LocalDate start, LocalDate end) {
        var startDateTime = start.atStartOfDay();
        var endDateTime = end.plusDays(1).atStartOfDay();

        // Totales dentro del rango seleccionado
        long totalVideos = videoGeneradoIARepository.countByCreatedAtBetween(startDateTime, endDateTime);
        long totalVehiculos = vehiculoRepository.countByCreatedAtBetween(startDateTime, endDateTime);
        long totalAnalisis = contenidoAnalizadoIARepository.countByCreatedAtBetween(startDateTime, endDateTime);
        long totalLogros = usuarioLogroRepository.countByFechaDesbloqueoBetween(startDateTime, endDateTime);
        long nuevosUsuarios = userRepository.countByCreatedAtBetween(java.sql.Date.valueOf(start), java.sql.Date.valueOf(end));

        long usuariosActivos = userRepository.countByActiveTrue();
        long usuariosInactivos = userRepository.countByActiveFalse();

        return new AdminMetricasDTO(
                totalVideos,
                totalVehiculos,
                totalAnalisis,
                totalLogros,
                nuevosUsuarios,
                usuariosActivos,
                usuariosInactivos
        );
    }

    @Override
    public AdminMetricasDTO getAdminMetricsGlobal() {

        long totalVideos = videoGeneradoIARepository.count();
        long totalVehiculos = vehiculoRepository.count();
        long totalAnalisis = contenidoAnalizadoIARepository.count();
        long totalLogros = usuarioLogroRepository.count();

        long nuevosUsuarios = userRepository.count();
        long usuariosActivos = userRepository.countByActiveTrue();
        long usuariosInactivos = userRepository.countByActiveFalse();

        return new AdminMetricasDTO(
                totalVideos,
                totalVehiculos,
                totalAnalisis,
                totalLogros,
                nuevosUsuarios,
                usuariosActivos,
                usuariosInactivos
        );
    }
}