package com.streamsutp.streamsutp.model;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.EqualsAndHashCode; // Importa EqualsAndHashCode
import lombok.ToString; // Importa ToString

@Entity
@Table(name = "ordenes")
@Data // Genera getters, setters, toString, equals y hashCode
@NoArgsConstructor // Genera un constructor sin argumentos
public class Orden {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_usuario", nullable = false)
    @ToString.Exclude // Evita recursión infinita en toString
    @EqualsAndHashCode.Exclude // Evita recursión infinita en equals/hashCode
    private Usuario usuario;

    private LocalDateTime fechaOrden;

    @Column(name = "total_orden", nullable = false, precision = 10, scale = 2)
    private BigDecimal totalOrden;
    @Column(name = "estado_orden", nullable = false)
    @Enumerated(EnumType.STRING)
    private EstadoOrden estadoOrden;

    private String direccionEnvio;
    private String ciudadEnvio;
    private String codigoPostalEnvio;

    @OneToMany(mappedBy = "orden", cascade = CascadeType.ALL, orphanRemoval = true)
    @ToString.Exclude // Evita recursión infinita en toString
    @EqualsAndHashCode.Exclude // Evita recursión infinita en equals/hashCode
    private List<DetalleOrden> detalles = new ArrayList<>();

    // Constructor personalizado para inicializar fecha y estado por defecto
    public Orden(Usuario usuario, BigDecimal totalOrden, String direccionEnvio, String ciudadEnvio, String codigoPostalEnvio) {
        this.usuario = usuario;
        this.fechaOrden = LocalDateTime.now();
        this.totalOrden = totalOrden;
        this.estadoOrden = EstadoOrden.PENDIENTE;
        this.direccionEnvio = direccionEnvio;
        this.ciudadEnvio = ciudadEnvio;
        this.codigoPostalEnvio = codigoPostalEnvio;
    }

    // Método de utilidad para añadir detalles a la orden
    public void addDetalle(DetalleOrden detalle) {
        this.detalles.add(detalle);
        detalle.setOrden(this);
    }
}