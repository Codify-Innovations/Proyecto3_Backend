package com.project.demo.rest.vehiculo;

import com.project.demo.logic.entity.http.GlobalResponseHandler;
import com.project.demo.logic.entity.http.Meta;
import com.project.demo.logic.entity.services.logros.LogrosService;
import com.project.demo.logic.entity.user.UserRepository;
import com.project.demo.logic.entity.vehiculo.Vehiculo;
import com.project.demo.logic.entity.vehiculo.VehiculoRepository;
import com.project.demo.logic.entity.user.User;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("/api/vehiculos")
public class VehiculoRestController {

    @Autowired
    private VehiculoRepository vehiculoRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private LogrosService logrosService;

    @PostMapping("/{id}")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<?> addVehiculo(
            @PathVariable("id") Long usuarioId,
            @RequestBody Vehiculo vehiculo,
            HttpServletRequest request) {

        vehiculo.setUsuarioId(usuarioId);
        vehiculoRepository.save(vehiculo);

        User usuario = userRepository.findById(usuarioId)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado."));

        logrosService.evaluateAchievementsForUser(usuario);

        return new GlobalResponseHandler().handleResponse(
                "Vehículo registrado correctamente y logros evaluados para el usuario: " + usuarioId,
                vehiculo,
                HttpStatus.CREATED,
                request
        );
    }
}
