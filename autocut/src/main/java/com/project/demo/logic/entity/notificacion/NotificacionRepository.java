package com.project.demo.logic.entity.notificacion;

import com.project.demo.logic.entity.user.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface NotificacionRepository extends JpaRepository<Notificacion, Long> {

    List<Notificacion> findByUsuarioOrderByFechaDesc(User usuario);

    List<Notificacion> findByUsuarioAndLeidaFalse(User usuario);
}