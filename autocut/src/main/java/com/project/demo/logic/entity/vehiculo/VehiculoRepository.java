package com.project.demo.logic.entity.vehiculo;

import com.project.demo.logic.entity.user.User;
import com.project.demo.logic.entity.videoGeneradoIA.VideoGeneradoIA;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.List;

public interface VehiculoRepository extends JpaRepository<Vehiculo, Long> {

    Page<Vehiculo> findByUsuarioId(Long usuarioId, Pageable pageable);

 
    List<Vehiculo> findByUsuarioId(Long usuarioId);

    List<Vehiculo> findByMarcaContainingIgnoreCase(String marca);
    List<Vehiculo> findByModeloContainingIgnoreCase(String modelo);

    long countByUsuarioId(Long usuarioId);

    long countByUsuarioIdAndCategoria(Long usuarioId, String categoria);

    long countByUsuarioIdAndMarca(Long usuarioId, String marca);

    List<Vehiculo> findByUsuarioIdAndCreatedAtBetween(Long userId, LocalDateTime start, LocalDateTime end);
}

