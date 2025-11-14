package com.project.demo.logic.entity.logro;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface LogroRepository extends JpaRepository<Logro, Long> {

    List<Logro> findByActivoTrue();

    Optional<Logro> findByNombre(String nombre);

    List<Logro> findByCategoria(String categoria);

    List<Logro> findByCriterio(String criterio);
}