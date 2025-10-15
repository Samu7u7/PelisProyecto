package com.streamsutp.streamsutp.controller;

import com.streamsutp.streamsutp.dto.OrdenDTO;
import com.streamsutp.streamsutp.model.EstadoOrden;
import com.streamsutp.streamsutp.service.OrdenService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Controlador REST para la gestión de ventas por parte de un Administrador.
 * Devuelve datos en formato DTO (Data Transfer Object) para evitar errores
 * de serialización y controlar la información expuesta.
 */
@RestController
@RequestMapping("/api/admin/ventas")
@PreAuthorize("hasRole('ADMIN')")
@CrossOrigin(origins = "*") // Permitir peticiones desde cualquier origen
public class AdminVentasRestController {

    private final OrdenService ordenService;

    public AdminVentasRestController(OrdenService ordenService) {
        this.ordenService = ordenService;
    }

    @GetMapping
    public ResponseEntity<?> buscarOrdenes(@RequestParam(required = false) String estado) {
        try {
            List<OrdenDTO> ordenes;
            if (estado != null && !estado.isEmpty()) {
                try {
                    EstadoOrden estadoEnum = EstadoOrden.valueOf(estado.toUpperCase());
                    ordenes = ordenService.findOrdenesByEstadoAsDTO(estadoEnum);
                } catch (IllegalArgumentException e) {
                    return ResponseEntity.badRequest()
                        .body("Estado de orden inválido: " + estado);
                }
            } else {
                ordenes = ordenService.findAllOrdenesAsDTO();
            }
            return ResponseEntity.ok(ordenes);
        } catch (Exception e) {
            return ResponseEntity.internalServerError()
                .body("Error al procesar la solicitud: " + e.getMessage());
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<OrdenDTO> obtenerOrdenPorId(@PathVariable Long id) {
        return ordenService.findOrdenByIdAsDTO(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PutMapping("/{id}/estado")
    public ResponseEntity<OrdenDTO> actualizarEstadoOrden(@PathVariable Long id, @RequestBody ActualizarEstadoRequest request) {
        try {
            OrdenDTO ordenActualizada = ordenService.actualizarEstadoOrdenAsDTO(id, request.getNuevoEstado());
            return ResponseEntity.ok(ordenActualizada);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().build();
        }
    }


    // Clase auxiliar para mapear el cuerpo de la petición de actualización de estado
    public static class ActualizarEstadoRequest {
        private EstadoOrden nuevoEstado;
        public EstadoOrden getNuevoEstado() { return nuevoEstado; }
        // CORRECCIÓN DEL TYPO: 'nuevoado' -> 'nuevoEstado'
        public void setNuevoEstado(EstadoOrden nuevoEstado) { this.nuevoEstado = nuevoEstado; }
    }
}