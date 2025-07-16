package com.streamsutp.streamsutp.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "peliculas")
public class Pelicula {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    private String titulo;
    private String imagen;
    @Column(name = "precioComprar")
    private BigDecimal precioComprar;
    @Column(name = "precioAlquilar")
    private BigDecimal precioAlquilar;

    // --- AÑADE ESTA LÍNEA ---
    private int stock;
}