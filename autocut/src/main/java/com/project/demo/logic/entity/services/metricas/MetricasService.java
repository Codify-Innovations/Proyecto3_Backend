package com.project.demo.logic.entity.services.metricas;

import java.time.LocalDate;
import java.util.Map;

public interface MetricasService {
    Map<String, Object> getSummary(Long userId);

    Map<String, Object> getMetricsByDateRange(Long userId, LocalDate start, LocalDate end);

    AdminMetricasDTO getAdminMetrics(LocalDate start, LocalDate end);
}
