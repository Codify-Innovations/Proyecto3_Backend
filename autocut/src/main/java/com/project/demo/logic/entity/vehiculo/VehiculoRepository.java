package com.project.demo.logic.entity.vehiculo;

import com.project.demo.logic.entity.user.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface VehiculoRepository extends JpaRepository<Vehiculo, Long> {
    List<Vehiculo> findByUsuarioId(int usuarioId);
    List<Vehiculo> findByMarcaContainingIgnoreCase(String marca);
    List<Vehiculo> findByModeloContainingIgnoreCase(String modelo);

    long countByUsuarioId(Long usuarioId);

    long countByUsuarioIdAndCategoria(Long usuarioId, String categoria);

    long countByUsuarioIdAndMarca(Long usuarioId, String marca);
}
