package com.streamsutp.streamsutp.repository;

import com.streamsutp.streamsutp.model.DetalleOrden;
import com.streamsutp.streamsutp.model.Orden;
import com.streamsutp.streamsutp.model.Pelicula; // <--- ¡CAMBIO AQUÍ! Importamos Pelicula
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DetalleOrdenRepository extends JpaRepository<DetalleOrden, Long> {
    // Puedes añadir métodos personalizados aquí:
    // Para encontrar los detalles de una orden específica
    List<DetalleOrden> findByOrden(Orden orden);

    // Para encontrar detalles de órdenes que contengan una película específica
    List<DetalleOrden> findByPelicula(Pelicula pelicula); // <--- ¡CAMBIO AQUÍ! Ahora es findByPelicula
}