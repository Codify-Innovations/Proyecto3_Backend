package com.project.demo.rest.logros;


import com.project.demo.logic.entity.http.GlobalResponseHandler;
import com.project.demo.logic.entity.logro.Logro;
import com.project.demo.logic.entity.services.logros.LogrosService;
import com.project.demo.logic.entity.user.User;
import com.project.demo.logic.entity.user.UserRepository;
import com.project.demo.logic.entity.usuarioLogro.UsuarioLogro;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.List;

@RestController
@RequestMapping("/api/logros")
public class LogrosRestController {

    @Autowired
    private LogrosService logrosService;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private GlobalResponseHandler responseHandler;


    @GetMapping("/activos")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<?> getActiveAchievements(HttpServletRequest request) {
        List<Logro> logros = logrosService.getAllActiveAchievements();
        return responseHandler.handleResponse(
                "Logros activos obtenidos correctamente.",
                logros,
                HttpStatus.OK,
                request
        );
    }

    @GetMapping("/mis-logros")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<?> getMyAchievements(HttpServletRequest request) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        User usuario = (User) auth.getPrincipal();

        List<UsuarioLogro> logrosUsuario = logrosService.getUnlockedAchievements(usuario);

        return responseHandler.handleResponse(
                "Logros del usuario obtenidos correctamente.",
                logrosUsuario,
                HttpStatus.OK,
                request
        );
    }

    @GetMapping("/usuario/{usuarioId}")
    public ResponseEntity<?> getPublicAchievements(
            @PathVariable Long usuarioId,
            HttpServletRequest request) {

        User usuario = userRepository.findById(usuarioId)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado."));

        if (!"public".equalsIgnoreCase(usuario.getVisibility())) {
            return responseHandler.handleResponse(
                    "El perfil del usuario es privado.",
                    HttpStatus.FORBIDDEN,
                    request
            );
        }

        List<UsuarioLogro> logrosUsuario = logrosService.getUnlockedAchievements(usuario);

        return responseHandler.handleResponse(
                "Logros públicos obtenidos correctamente.",
                logrosUsuario,
                HttpStatus.OK,
                request
        );
    }
}