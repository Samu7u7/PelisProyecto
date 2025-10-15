package com.streamsutp.streamsutp.dto;

import java.math.BigDecimal;
import com.streamsutp.streamsutp.model.TipoVenta;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * DTO para los detalles de una línea de orden.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class DetalleOrdenDTO {
    private Long id;
    private PeliculaDTO pelicula;
    private Integer cantidad;
    private BigDecimal precioUnitario;
    private TipoVenta tipoVenta;
    private BigDecimal subtotal;
}