package com.project.demo.logic.entity.usuarioLogro;

import com.project.demo.logic.entity.logro.Logro;
import com.project.demo.logic.entity.user.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UsuarioLogroRepository extends JpaRepository<UsuarioLogro, Long> {

    List<UsuarioLogro> findByUsuario(User usuario);

    Optional<UsuarioLogro> findByUsuarioAndLogro(User usuario, Logro logro);

    boolean existsByUsuarioAndLogro(User usuario, Logro logro);
}