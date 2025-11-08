package com.project.demo.logic.entity.vehiculo;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface VehiculoRepository extends JpaRepository<Vehiculo, Long> {
    List<Vehiculo> findByUsuarioId(int usuarioId);
    List<Vehiculo> findByMarcaContainingIgnoreCase(String marca);
    List<Vehiculo> findByModeloContainingIgnoreCase(String modelo);
}
