package com.streamsutp.streamsutp.repository;

import com.streamsutp.streamsutp.model.Pelicula; // Importa tu clase Pelicula
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PeliculaRepository extends JpaRepository<Pelicula, Long> {
    // Aquí puedes añadir métodos personalizados si los necesitas,
    // por ejemplo: List<Pelicula> findByTituloContaining(String titulo);
}