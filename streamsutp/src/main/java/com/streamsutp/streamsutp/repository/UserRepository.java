package com.streamsutp.streamsutp.repository;

import com.streamsutp.streamsutp.model.Usuario; // Asegúrate de que esta importación sea correcta
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional; // ¡¡¡IMPORTA ESTO!!!

@Repository
public interface UserRepository extends JpaRepository<Usuario, Long> {
    // Estos métodos le dicen a Spring Data JPA que debe buscar un Usuario
    // por su nombre de usuario o por su email, y devolver un Optional.
    // Optional significa que el resultado puede estar presente o no,
    // lo cual manejas con .isPresent() o .orElseThrow().

    Optional<Usuario> findByUsername(String username); // <--- ¡Añade o modifica esta línea!
    Optional<Usuario> findByEmail(String email);       // <--- ¡Añade esta línea si no la tienes!
}