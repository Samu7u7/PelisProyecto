package com.streamsutp.streamsutp.dto;

import com.streamsutp.streamsutp.model.EstadoOrden;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * DTO principal para la entidad Orden. Este es el objeto que será
 * devuelto por la API REST. Es un objeto "limpio" sin conexiones
 * a la base de datos ni carga perezosa.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class OrdenDTO {
    private Long id;
    private UsuarioDTO usuario;
    private LocalDateTime fechaOrden;
    private BigDecimal totalOrden;
    private EstadoOrden estadoOrden;
    private List<DetalleOrdenDTO> detalles;
}