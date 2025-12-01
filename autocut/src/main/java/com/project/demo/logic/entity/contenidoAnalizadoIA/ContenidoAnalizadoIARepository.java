package com.project.demo.logic.entity.contenidoAnalizadoIA;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDateTime;
import java.util.List;

public interface ContenidoAnalizadoIARepository extends JpaRepository<ContenidoAnalizadoIA, Long> {

    long countByUsuarioId(Long userId);

    @Query("SELECT AVG(c.score) FROM ContenidoAnalizadoIA c WHERE c.usuario.id = :userId")
    Double averageScoreByUsuarioId(Long userId);

    List<ContenidoAnalizadoIA> findByUsuarioIdAndCreatedAtBetween(Long userId, LocalDateTime start, LocalDateTime end);

    long countByCreatedAtBetween(LocalDateTime start, LocalDateTime end);

}
