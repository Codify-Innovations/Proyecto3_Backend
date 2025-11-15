package com.project.demo.rest.vehicle;

import com.project.demo.logic.entity.http.GlobalResponseHandler;
import com.project.demo.logic.entity.vehicle.VehicleCustomization;
import com.project.demo.logic.entity.vehicle.VehicleCustomizationMapper;
import com.project.demo.logic.entity.vehicle.VehicleCustomizationService;
import com.project.demo.logic.entity.user.User;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/vehicles/customizations")
public class VehicleCustomizationRestController {

    private final VehicleCustomizationService service;

    public VehicleCustomizationRestController(VehicleCustomizationService service) {
        this.service = service;
    }

    // Crear o actualizar una configuración de vehículo
    @PostMapping
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<?> saveCustomization(@RequestBody VehicleCustomization customization,
                                               HttpServletRequest request) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        User user = (User) auth.getPrincipal();

        VehicleCustomization saved = service.saveCustomization(user.getId(), customization);

        return new GlobalResponseHandler().handleResponse(
                "Configuración guardada correctamente.",
                saved,
                HttpStatus.OK,
                request
        );
    }

    // Obtener la configuración del usuario autenticado
    @GetMapping("/me")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<?> getMyCustomization(HttpServletRequest request) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        User user = (User) auth.getPrincipal();

        VehicleCustomization customization = service.getCustomizationByUser(user.getId());

        var dto = VehicleCustomizationMapper.toDto(customization);

        return new GlobalResponseHandler().handleResponse(
                "Configuración obtenida correctamente.",
                dto,
                HttpStatus.OK,
                request
        );
    }
}
