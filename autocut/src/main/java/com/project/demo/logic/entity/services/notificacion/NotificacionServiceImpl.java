package com.project.demo.logic.entity.services.notificacion;

import com.project.demo.logic.entity.notificacion.Notificacion;
import com.project.demo.logic.entity.notificacion.NotificacionRepository;
import com.project.demo.logic.entity.user.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class NotificacionServiceImpl implements  NotificacionService{
    @Autowired
    private NotificacionRepository notificacionRepository;

    public void crearNotificacion(User usuario, String mensaje, String tipo) {
        Notificacion notificacion = new Notificacion();
        notificacion.setMensaje(mensaje);
        notificacion.setTipo(tipo);
        notificacion.setLeida(false);
        notificacion.setFecha(LocalDateTime.now());

        notificacion.setUsuario(usuario);

        notificacionRepository.save(notificacion);
    }

    public List<Notificacion> getNoLeidas(User usuario) {
        return notificacionRepository.findByUsuarioAndLeidaFalse(usuario);
    }

    public void marcarLeida(Long id) {
        Notificacion n = notificacionRepository.findById(id).orElse(null);
        if (n != null) {
            n.setLeida(true);
            notificacionRepository.save(n);
        }
    }
}