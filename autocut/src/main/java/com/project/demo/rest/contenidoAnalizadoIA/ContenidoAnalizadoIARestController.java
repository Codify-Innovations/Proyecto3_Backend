package com.project.demo.rest.contenidoAnalizadoIA;

import com.project.demo.logic.entity.contenidoAnalizadoIA.ContenidoAnalizadoIA;
import com.project.demo.logic.entity.http.GlobalResponseHandler;
import com.project.demo.logic.entity.services.contenidoAnalizadoIA.ContenidoAnalizadoIADTO;
import com.project.demo.logic.entity.services.contenidoAnalizadoIA.ContenidoAnalizadoIAService;
import com.project.demo.logic.entity.user.User;
import com.project.demo.logic.entity.user.UserRepository;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;

@RestController
@RequestMapping("/api/ia/analysis")
public class ContenidoAnalizadoIARestController {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ContenidoAnalizadoIAService contenidoAnalizadoIAService;

    @PostMapping("/{usuarioId}")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<?> registrarAnalisis(
            @PathVariable("usuarioId") Long usuarioId,
            @RequestBody ContenidoAnalizadoIADTO request,
            HttpServletRequest httpRequest
    ) {

        User usuario = userRepository.findById(usuarioId)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado."));

        ContenidoAnalizadoIA registro = new ContenidoAnalizadoIA();
        registro.setUsuario(usuario);
        registro.setSourceUrl(request.sourceUrl());
        registro.setAnalysisType(request.analysisType());
        registro.setScore(request.score());
        registro.setCreatedAt(LocalDateTime.now());

        contenidoAnalizadoIAService.save(registro);

        return new GlobalResponseHandler().handleResponse(
                "Análisis registrado correctamente para el usuario: " + usuarioId,
                registro,
                HttpStatus.CREATED,
                httpRequest
        );
    }
}
