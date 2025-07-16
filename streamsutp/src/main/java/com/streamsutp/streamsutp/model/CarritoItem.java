package com.streamsutp.streamsutp.model; // O en un paquete 'dto' si lo creas: com.streamsutp.streamsutp.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.math.BigDecimal; // ¡Importante para dinero!

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CarritoItem implements Serializable {
    private Long peliculaId; // ¡NUEVO! Para saber qué película es en la base de datos
    private String titulo;
    private String tipo;     // Sigue siendo String, por ejemplo "ALQUILER" o "COMPRA"
    private BigDecimal precio; // ¡CAMBIO! Ahora es BigDecimal
    private String imagen;
    private int cantidad;    // ¡NUEVO! Para la cantidad de unidades (generalmente 1 para películas)
}