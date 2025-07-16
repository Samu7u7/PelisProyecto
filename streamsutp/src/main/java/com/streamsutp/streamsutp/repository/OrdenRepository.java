package com.streamsutp.streamsutp.repository;

import com.streamsutp.streamsutp.model.Orden;
import com.streamsutp.streamsutp.model.Usuario; // Necesario si quieres buscar por usuario
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
// import java.util.Optional;

@Repository
public interface OrdenRepository extends JpaRepository<Orden, Long> {
    // Puedes añadir métodos personalizados aquí:
    // Para encontrar órdenes de un usuario específico
    List<Orden> findByUsuario(Usuario usuario);

    // Para encontrar órdenes por estado
    List<Orden> findByEstadoOrden(com.streamsutp.streamsutp.model.EstadoOrden estado);

    // Para encontrar las últimas N órdenes
    // List<Orden> findTop10ByOrderByFechaOrdenDesc();
}