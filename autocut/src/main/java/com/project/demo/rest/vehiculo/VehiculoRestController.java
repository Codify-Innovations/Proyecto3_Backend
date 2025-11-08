package com.project.demo.rest.vehiculo;

import com.project.demo.logic.entity.http.GlobalResponseHandler;
import com.project.demo.logic.entity.http.Meta;
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


//    @GetMapping
//    @PreAuthorize("hasAnyRole('SUPER_ADMIN_ROLE')")
//    public ResponseEntity<?> getAll(
//            @RequestParam(defaultValue = "1") int page,
//            @RequestParam(defaultValue = "10") int size,
//            HttpServletRequest request) {
//
//        Pageable pageable = PageRequest.of(page - 1, size);
//        Page<Vehiculo> vehiculosPage = vehiculoRepository.findAll(pageable);
//
//        Meta meta = new Meta(request.getMethod(), request.getRequestURL().toString());
//        meta.setTotalPages(vehiculosPage.getTotalPages());
//        meta.setTotalElements(vehiculosPage.getTotalElements());
//        meta.setPageNumber(vehiculosPage.getNumber() + 1);
//        meta.setPageSize(vehiculosPage.getSize());
//
//        return new GlobalResponseHandler().handleResponse(
//                "Vehículos obtenidos correctamente.",
//                vehiculosPage.getContent(),
//                HttpStatus.OK,
//                meta
//        );
//    }

//    /**
//     * GET /vehiculos/mis → Lista los vehículos del usuario autenticado
//     */
//    @GetMapping("/mis")
//    @PreAuthorize("isAuthenticated()")
//    public ResponseEntity<?> getMyVehicles(HttpServletRequest request) {
//        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
//        User user = (User) auth.getPrincipal();
//
//        var vehiculos = vehiculoRepository.findByUsuarioId(user.getId());
//
//        return new GlobalResponseHandler().handleResponse(
//                "Vehículos del usuario obtenidos correctamente.",
//                vehiculos,
//                HttpStatus.OK,
//                request
//        );
//    }


    // ===== VALIDACIONES DE EXISTENCIA DE USUARIO ===== //
    @PostMapping("/{id}")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<?> addVehiculo(
            @PathVariable("id") Long usuarioId,
            @RequestBody Vehiculo vehiculo,
            HttpServletRequest request) {

        vehiculo.setUsuarioId(usuarioId);

        vehiculoRepository.save(vehiculo);

        return new GlobalResponseHandler().handleResponse(
                "Vehículo registrado correctamente para el usuario con ID " + usuarioId,
                vehiculo,
                HttpStatus.CREATED,
                request
        );
    }


//    /**
//     * PUT /vehiculos/{id} → Actualiza un vehículo del usuario autenticado
//     */
//    @PutMapping("/{id}")
//    @PreAuthorize("isAuthenticated()")
//    public ResponseEntity<?> updateVehiculo(@PathVariable int id, @RequestBody Vehiculo vehiculo, HttpServletRequest request) {
//        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
//        User user = (User) auth.getPrincipal();
//
//        Optional<Vehiculo> existing = vehiculoRepository.findById(id);
//        if (existing.isEmpty()) {
//            return new GlobalResponseHandler().handleResponse(
//                    "Vehículo con ID " + id + " no encontrado.",
//                    HttpStatus.NOT_FOUND,
//                    request
//            );
//        }
//
//        Vehiculo found = existing.get();
//
//        // Validar que el vehículo pertenece al usuario autenticado
//        if (found.getUsuarioId() != user.getId()) {
//            return new GlobalResponseHandler().handleResponse(
//                    "No tienes permiso para modificar este vehículo.",
//                    HttpStatus.FORBIDDEN,
//                    request
//            );
//        }
//
//        // Actualizar solo los campos editables
//        if (vehiculo.getMarca() != null) found.setMarca(vehiculo.getMarca());
//        if (vehiculo.getModelo() != null) found.setModelo(vehiculo.getModelo());
//        if (vehiculo.getAnio() != 0) found.setAnio(vehiculo.getAnio());
//        if (vehiculo.getConfianzaId() != null) found.setConfianzaId(vehiculo.getConfianzaId());
//        if (vehiculo.getImagenUri() != null) found.setImagenUri(vehiculo.getImagenUri());
//
//        vehiculoRepository.save(found);
//
//        return new GlobalResponseHandler().handleResponse(
//                "Vehículo actualizado correctamente.",
//                found,
//                HttpStatus.OK,
//                request
//        );
//    }
//
//    /**
//     * DELETE /vehiculos/{id} → Elimina un vehículo del usuario autenticado
//     */
//    @DeleteMapping("/{id}")
//    @PreAuthorize("isAuthenticated()")
//    public ResponseEntity<?> deleteVehiculo(@PathVariable int id, HttpServletRequest request) {
//        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
//        User user = (User) auth.getPrincipal();
//
//        Optional<Vehiculo> existing = vehiculoRepository.findById(id);
//        if (existing.isEmpty()) {
//            return new GlobalResponseHandler().handleResponse(
//                    "Vehículo con ID " + id + " no encontrado.",
//                    HttpStatus.NOT_FOUND,
//                    request
//            );
//        }
//
//        Vehiculo found = existing.get();
//
//        if (found.getUsuarioId() != user.getId()) {
//            return new GlobalResponseHandler().handleResponse(
//                    "No tienes permiso para eliminar este vehículo.",
//                    HttpStatus.FORBIDDEN,
//                    request
//            );
//        }
//
//        vehiculoRepository.delete(found);
//
//        return new GlobalResponseHandler().handleResponse(
//                "Vehículo eliminado correctamente.",
//                found,
//                HttpStatus.OK,
//                request
//        );
//    }
}
