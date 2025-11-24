package com.project.demo.logic.entity.services.notificacion;

import com.project.demo.logic.entity.notificacion.Notificacion;
import com.project.demo.logic.entity.user.User;

import java.util.List;

public interface NotificacionService {
    void crearNotificacion(User usuario, String mensaje, String tipo);
    void marcarLeida(Long id);
    List<Notificacion> getNoLeidas(User usuario);
}