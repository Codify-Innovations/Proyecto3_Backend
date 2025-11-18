package com.project.demo.logic.entity.services.notificacion;

import com.project.demo.logic.entity.user.User;

public interface NotificacionService {
    public void crearNotificacion(User usuario, String mensaje, String tipo);
    void marcarLeida(Long id);
}