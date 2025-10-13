package com.streamsutp.streamsutp.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.streamsutp.streamsutp.model.Subscripcion;
import com.streamsutp.streamsutp.model.Usuario;

@Repository
public interface SubscripcionRepository extends JpaRepository<Subscripcion, Long> {
    // Encontrar todas las suscripciones de un usuario
    List<Subscripcion> findByUsuario(Usuario usuario);

    // Encontrar la suscripción activa de un usuario
    List<Subscripcion> findByUsuarioAndStatus(Usuario usuario, Subscripcion.Status status);
}
