package com.project.demo.logic.entity.logro;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface LogroRepository extends JpaRepository<Logro, Long> {

    List<Logro> findByActivoTrue();

    List<Logro> findByCategoria(String categoria);

    List<Logro> findByCriterio(String criterio);
}