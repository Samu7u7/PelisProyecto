package com.streamsutp.streamsutp.model;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType; // Importa EqualsAndHashCode
import jakarta.persistence.Enumerated; // Importa ToString
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;

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
    private EstadoOrden estadoOrden= EstadoOrden.PENDIENTE;

    //private String direccionEnvio;
    //private String ciudadEnvio;
    //private String codigoPostalEnvio;

    @OneToMany(mappedBy = "orden", cascade = CascadeType.ALL, orphanRemoval = true)
    @ToString.Exclude // Evita recursión infinita en toString
    @EqualsAndHashCode.Exclude // Evita recursión infinita en equals/hashCode
    private List<DetalleOrden> detalles = new ArrayList<>();

    // Constructor personalizado para inicializar fecha y estado por defecto
    public Orden(Usuario usuario, BigDecimal totalOrden) {
        this.usuario = usuario;
        this.fechaOrden = LocalDateTime.now();
        this.totalOrden = totalOrden;
        this.estadoOrden = EstadoOrden.PENDIENTE;
    }

    // Método de utilidad para añadir detalles a la orden
    public void addDetalle(DetalleOrden detalle) {
        this.detalles.add(detalle);
        detalle.setOrden(this);
    }
}