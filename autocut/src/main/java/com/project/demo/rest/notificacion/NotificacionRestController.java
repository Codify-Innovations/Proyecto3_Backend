package com.project.demo.rest.notificacion;

import com.project.demo.logic.entity.http.GlobalResponseHandler;
import com.project.demo.logic.entity.notificacion.Notificacion;
import com.project.demo.logic.entity.services.notificacion.NotificacionService;
import com.project.demo.logic.entity.user.User;
import com.project.demo.logic.entity.user.UserRepository;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/notificaciones")
public class NotificacionRestController {

    @Autowired
    private NotificacionService notificacionService;

    @Autowired
    private UserRepository userRepository;

    @GetMapping("/no-leidas/{usuarioId}")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<?> getNoLeidas(
            @PathVariable("usuarioId") Long usuarioId,
            HttpServletRequest request) {

        User usuario = userRepository.findById(usuarioId)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado."));

        List<Notificacion> notificaciones = notificacionService.getNoLeidas(usuario);

        return new GlobalResponseHandler().handleResponse(
                "Notificaciones no leídas obtenidas correctamente para el usuario: " + usuarioId,
                notificaciones,
                HttpStatus.OK,
                request
        );
    }

    @PutMapping("/marcar-leida/{id}")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<?> marcarLeida(
            @PathVariable("id") Long notificacionId,
            HttpServletRequest request) {

        notificacionService.marcarLeida(notificacionId);

        return new GlobalResponseHandler().handleResponse(
                "Notificación marcada como leída correctamente. ID: " + notificacionId,
                null,
                HttpStatus.OK,
                request
        );
    }
}
