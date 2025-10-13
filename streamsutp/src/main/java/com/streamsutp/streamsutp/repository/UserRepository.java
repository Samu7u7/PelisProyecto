package com.streamsutp.streamsutp.repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional; // Asegúrate de que esta importación sea correcta

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.streamsutp.streamsutp.model.Usuario; // ¡¡¡IMPORTA ESTO!!!

@Repository
public interface UserRepository extends JpaRepository<Usuario, Long> {
    // Estos métodos le dicen a Spring Data JPA que debe buscar un Usuario
    // por su nombre de usuario o por su email, y devolver un Optional.
    // Optional significa que el resultado puede estar presente o no,
    // lo cual manejas con .isPresent() o .orElseThrow().

    Optional<Usuario> findByUsername(String username); // <--- ¡Añade o modifica esta línea!
    Optional<Usuario> findByEmail(String email);       // <--- ¡Añade esta línea si no la tienes!


    @Query("""
      select u from Usuario u join u.subscripciones s where s.status = 'ACTIVE' and s.fechaFin > :now""")
    List<Usuario> findUsersWithActiveSubscription(@Param("now") LocalDateTime now);
}