package com.project.demo.logic.entity.videoGeneradoIA;

import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.List;

public interface VideoGeneradoIARepository extends JpaRepository<VideoGeneradoIA,Long> {

    long countByUsuarioId(Long userId);

    List<VideoGeneradoIA> findByUsuarioIdAndCreatedAtBetween(Long userId, LocalDateTime start, LocalDateTime end);

    long countByCreatedAtBetween(LocalDateTime start, LocalDateTime end);
}
