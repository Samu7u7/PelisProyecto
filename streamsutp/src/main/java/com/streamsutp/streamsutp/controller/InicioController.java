package com.streamsutp.streamsutp.controller;

import com.streamsutp.streamsutp.model.Pelicula;
import com.streamsutp.streamsutp.repository.PeliculaRepository;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.security.core.Authentication; // ¡Importa esto!
import org.springframework.security.core.context.SecurityContextHolder; // ¡Importa esto!

import java.util.List;

@Controller
public class InicioController {

    private final PeliculaRepository peliculaRepository;

    // Constructor para inyectar el repositorio
    public InicioController(PeliculaRepository peliculaRepository) {
        this.peliculaRepository = peliculaRepository;
    }

    @GetMapping({"/", "/index"})
    public String mostrarIndex(Model model) {
        // --- ¡PON EL BREAKPOINT EN LA LÍNEA DE ABAJO! ---
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication != null && authentication.isAuthenticated()) {
            System.out.println("DEBUG: Usuario autenticado: " + authentication.getName() + " con roles: " + authentication.getAuthorities());
            model.addAttribute("usuarioAutenticadoNombre", authentication.getName());
            model.addAttribute("usuarioEstaAutenticado", true);
        } else {
            System.out.println("DEBUG: Usuario NO autenticado o anónimo.");
            model.addAttribute("usuarioAutenticadoNombre", "Anónimo");
            model.addAttribute("usuarioEstaAutenticado", false);
        }
        // --- FIN DEL BLOQUE DE DEBUG ---


        // Carga las películas de la base de datos
        List<Pelicula> peliculas = peliculaRepository.findAll();
        model.addAttribute("peliculas", peliculas);

        return "index";
    
    }
            // En tu Home o un nuevo CompraController
        @GetMapping("/confirmacion-compra")
        public String confirmacionCompra() {
            return "confirmacion-compra"; // Nombre de tu plantilla HTML
        }
}