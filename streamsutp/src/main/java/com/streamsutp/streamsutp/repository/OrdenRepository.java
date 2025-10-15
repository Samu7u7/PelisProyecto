package com.streamsutp.streamsutp.repository;

import com.streamsutp.streamsutp.model.Orden;
import com.streamsutp.streamsutp.model.Usuario;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OrdenRepository extends JpaRepository<Orden, Long> {

    /**
     * Sobrescribimos los métodos de búsqueda para usar un EntityGraph. 
     * Esto le dice a JPA que cargue las relaciones especificadas de forma ansiosa (EAGER)
     * en una sola consulta, resolviendo de forma definitiva los problemas de LazyInitializationException.
     * attributePaths: Especifica las relaciones a cargar: el usuario de la orden, la lista de detalles,
     * y dentro de cada detalle, la película asociada.
     */
    @Override
    @EntityGraph(attributePaths = { "usuario", "detalles.pelicula" })
    List<Orden> findAll();

    @EntityGraph(attributePaths = { "usuario", "detalles.pelicula" })
    List<Orden> findByEstadoOrden(com.streamsutp.streamsutp.model.EstadoOrden estado);

    // Este método no se usa en la API, así que no necesita el EntityGraph por ahora.
    List<Orden> findByUsuario(Usuario usuario);
}