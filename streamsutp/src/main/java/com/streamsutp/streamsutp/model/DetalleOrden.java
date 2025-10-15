package com.streamsutp.streamsutp.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.math.BigDecimal;

@Entity
@Table(name = "detalles_orden")
@Data
@NoArgsConstructor
public class DetalleOrden {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY) // Esta puede seguir siendo LAZY porque es una referencia "hacia atrás"
    @JoinColumn(name = "id_orden", nullable = false)
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    @JsonBackReference("orden-detalles")
    private Orden orden;

    // CAMBIO A EAGER: Forzamos la carga inmediata de la película para evitar errores de Lazy Loading.
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "id_pelicula", nullable = false)
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    @JsonManagedReference("pelicula-detalles")
    private Pelicula pelicula;

    private Integer cantidad;

    private BigDecimal precioUnitario;

    @Enumerated(EnumType.STRING)
    private TipoVenta tipoVenta;

    private BigDecimal subtotal;

    public DetalleOrden(Orden orden, Pelicula pelicula, Integer cantidad,
                        BigDecimal precioUnitario, TipoVenta tipoVenta) {
        this.orden = orden;
        this.pelicula = pelicula;
        this.cantidad = cantidad;
        this.precioUnitario = precioUnitario;
        this.tipoVenta = tipoVenta;
        this.subtotal = precioUnitario.multiply(BigDecimal.valueOf(cantidad));
    }

    @PrePersist
    @PreUpdate
    private void calcularSubtotal() {
        if (precioUnitario != null && cantidad != null) {
            this.subtotal = precioUnitario.multiply(BigDecimal.valueOf(cantidad));
        }
    }
}