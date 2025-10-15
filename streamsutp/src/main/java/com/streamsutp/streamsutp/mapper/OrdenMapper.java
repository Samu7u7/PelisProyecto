package com.streamsutp.streamsutp.mapper;

import com.streamsutp.streamsutp.dto.DetalleOrdenDTO;
import com.streamsutp.streamsutp.dto.OrdenDTO;
import com.streamsutp.streamsutp.dto.PeliculaDTO;
import com.streamsutp.streamsutp.dto.UsuarioDTO;
import com.streamsutp.streamsutp.model.DetalleOrden;
import com.streamsutp.streamsutp.model.Orden;
import com.streamsutp.streamsutp.model.Pelicula;
import com.streamsutp.streamsutp.model.Usuario;
import java.util.stream.Collectors;

/**
 * Clase de utilidad para mapear (convertir) entidades de la base de datos a DTOs.
 * Esto nos permite controlar exactamente qué información se expone en la API
 * y resuelve los problemas de carga perezosa (Lazy Loading).
 */
public class OrdenMapper {

    public static UsuarioDTO toUsuarioDTO(Usuario usuario) {
        return new UsuarioDTO(
            usuario.getId(),
            usuario.getUsername()
        );
    }

    public static PeliculaDTO toPeliculaDTO(Pelicula pelicula) {
        return new PeliculaDTO(
            pelicula.getId(),
            pelicula.getTitulo()
        );
    }

    public static DetalleOrdenDTO toDetalleOrdenDTO(DetalleOrden detalle) {
        return new DetalleOrdenDTO(
            detalle.getId(),
            toPeliculaDTO(detalle.getPelicula()), // Usamos el mapper de Pelicula
            detalle.getCantidad(),
            detalle.getPrecioUnitario(),
            detalle.getTipoVenta(),
            detalle.getSubtotal()
        );
    }

    public static OrdenDTO toOrdenDTO(Orden orden) {
        return new OrdenDTO(
            orden.getId(),
            toUsuarioDTO(orden.getUsuario()), // Usamos el mapper de Usuario
            orden.getFechaOrden(),
            orden.getTotalOrden(),
            orden.getEstadoOrden(),
            // Mapeamos cada detalle en la lista a su DTO correspondiente
            orden.getDetalles().stream()
                .map(OrdenMapper::toDetalleOrdenDTO)
                .collect(Collectors.toList())
        );
    }
}
