package com.streamsutp.streamsutp.model;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "ordenes")
@Data
@NoArgsConstructor
public class Orden {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // CAMBIO A EAGER: Forzamos la carga inmediata del usuario para evitar errores de Lazy Loading.
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "id_usuario", nullable = false)
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    @JsonManagedReference
    private Usuario usuario;

    private LocalDateTime fechaOrden;

    @Column(name = "total_orden", nullable = false, precision = 10, scale = 2)
    private BigDecimal totalOrden;

    @Column(name = "estado_orden", nullable = false)
    @Enumerated(EnumType.STRING)
    private EstadoOrden estadoOrden = EstadoOrden.PENDIENTE;

    // CAMBIO A EAGER: Forzamos la carga inmediata de los detalles para evitar errores de Lazy Loading.
    @OneToMany(mappedBy = "orden", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.EAGER)
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    @JsonManagedReference("orden-detalles")
    private List<DetalleOrden> detalles = new ArrayList<>();

    public Orden(Usuario usuario, BigDecimal totalOrden) {
        this.usuario = usuario;
        this.fechaOrden = LocalDateTime.now();
        this.totalOrden = totalOrden;
        this.estadoOrden = EstadoOrden.PENDIENTE;
    }

    public void addDetalle(DetalleOrden detalle) {
        this.detalles.add(detalle);
        detalle.setOrden(this);
    }
}