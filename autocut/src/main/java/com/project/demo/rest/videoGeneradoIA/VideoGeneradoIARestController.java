package com.project.demo.rest.videoGeneradoIA;

import com.project.demo.logic.entity.http.GlobalResponseHandler;
import com.project.demo.logic.entity.services.videoGeneradoIA.VideoGeneradoIADTO;
import com.project.demo.logic.entity.services.videoGeneradoIA.VideoGeneradoIAService;
import com.project.demo.logic.entity.videoGeneradoIA.VideoGeneradoIA;
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
@RequestMapping("/api/ia/videos")
public class VideoGeneradoIARestController {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private VideoGeneradoIAService videoGeneradoIAService;

    @PostMapping("/{usuarioId}")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<?> registrarVideoGenerado(
            @PathVariable("usuarioId") Long usuarioId,
            @RequestBody VideoGeneradoIADTO request,
            HttpServletRequest httpRequest
    ) {

        User usuario = userRepository.findById(usuarioId)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado."));

        VideoGeneradoIA video = new VideoGeneradoIA();
        video.setUsuario(usuario);
        video.setImageUrls(request.imageUrls());
        video.setStyle(request.style());
        video.setDuration(request.duration());
        video.setVideoUrl(request.videoUrl());
        video.setCreatedAt(LocalDateTime.now());

        videoGeneradoIAService.save(video);

        return new GlobalResponseHandler().handleResponse(
                "Video generado registrado correctamente para el usuario: " + usuarioId,
                video,
                HttpStatus.CREATED,
                httpRequest
        );
    }
}
