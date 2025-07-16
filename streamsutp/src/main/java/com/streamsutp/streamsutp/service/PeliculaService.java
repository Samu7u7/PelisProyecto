package com.streamsutp.streamsutp.service;

import com.streamsutp.streamsutp.model.Pelicula;
import com.streamsutp.streamsutp.repository.PeliculaRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service // Marca esta clase como un componente de servicio
public class PeliculaService {

    private final PeliculaRepository peliculaRepository;

    // Inyección de dependencia del repositorio a través del constructor
    public PeliculaService(PeliculaRepository peliculaRepository) {
        this.peliculaRepository = peliculaRepository;
    }

    // Método para guardar una nueva película (o actualizar una existente)
    public Pelicula guardarPelicula(Pelicula pelicula) {
        return peliculaRepository.save(pelicula);
    }

    // Método para obtener todas las películas
    public List<Pelicula> obtenerTodasLasPeliculas() {
        return peliculaRepository.findAll();
    }

    // Método para obtener una película por su ID
    public Optional<Pelicula> obtenerPeliculaPorId(Long id) {
        return peliculaRepository.findById(id);
    }

    // Método para eliminar una película por su ID
    public void eliminarPelicula(Long id) {
        peliculaRepository.deleteById(id);
    }

    // Puedes añadir más lógica de negocio aquí, por ejemplo:
    // public List<Pelicula> buscarPeliculasPorTitulo(String titulo) {
    //     return peliculaRepository.findByTituloContainingIgnoreCase(titulo);
    // }
}