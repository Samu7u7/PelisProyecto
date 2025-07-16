package com.streamsutp.streamsutp.model;

import jakarta.persistence.*;
import java.math.BigDecimal;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.ToString;

@Entity
@Table(name = "detalles_orden")
@Data // Genera getters, setters, toString, equals y hashCode
@NoArgsConstructor // Genera un constructor sin argumentos
public class DetalleOrden {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_orden", nullable = false)
    @ToString.Exclude // Evita recursión infinita
    @EqualsAndHashCode.Exclude // Evita recursión infinita
    private Orden orden;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_pelicula", nullable = false) // <--- ¡CAMBIO AQUÍ! Ahora apunta a 'id_pelicula'
    @ToString.Exclude // Evita recursión infinita
    @EqualsAndHashCode.Exclude // Evita recursión infinita
    private Pelicula pelicula; // <--- ¡CAMBIO AQUÍ! El tipo de objeto ahora es Pelicula

    private Integer cantidad;

    private BigDecimal precioUnitario; // Precio del producto al momento de la compra

    @Enumerated(EnumType.STRING)
    private TipoVenta tipoVenta;

    private BigDecimal subtotal;

    // Constructor personalizado para inicializar el subtotal
    public DetalleOrden(Orden orden, Pelicula pelicula, Integer cantidad, BigDecimal precioUnitario, TipoVenta tipoVenta) { // <--- ¡CAMBIO AQUÍ en el parámetro!
        this.orden = orden;
        this.pelicula = pelicula; // <--- ¡CAMBIO AQUÍ!
        this.cantidad = cantidad;
        this.precioUnitario = precioUnitario;
        this.tipoVenta = tipoVenta;
        this.subtotal = precioUnitario.multiply(new BigDecimal(cantidad));
    }

    // El resto de getters y setters son generados por Lombok @Data.
    // Si necesitas algún getter/setter específico que Lombok no maneje bien, lo añadirías manualmente.
    // Ejemplo:
    // public Pelicula getPelicula() { return pelicula; }
    // public void setPelicula(Pelicula pelicula) { this.pelicula = pelicula; }
}