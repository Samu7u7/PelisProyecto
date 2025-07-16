package com.streamsutp.streamsutp.controller;

import com.streamsutp.streamsutp.model.Pelicula;
import com.streamsutp.streamsutp.service.PeliculaService;
import jakarta.validation.Valid;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/admin/peliculas")
@PreAuthorize("hasRole('ADMIN')") // <-- Seguridad solo para administradores
public class PeliculaController {

    private final PeliculaService peliculaService;

    public PeliculaController(PeliculaService peliculaService) {
        this.peliculaService = peliculaService;
    }

    @GetMapping
    public String listarPeliculas(Model model) {
        model.addAttribute("peliculas", peliculaService.obtenerTodasLasPeliculas());
        return "admin/peliculas/lista"; // <-- Debes crear esta vista HTML
    }

    @GetMapping("/nuevo")
    public String mostrarFormularioNuevo(Model model) {
        model.addAttribute("pelicula", new Pelicula());
        return "admin/peliculas/formulario"; // <-- Debes crear esta vista HTML
    }

    @PostMapping("/guardar")
    public String guardarPelicula(@Valid @ModelAttribute("pelicula") Pelicula pelicula, BindingResult result, RedirectAttributes redirectAttributes) {
        if (result.hasErrors()) {
            return "admin/peliculas/formulario";
        }
        peliculaService.guardarPelicula(pelicula);
        redirectAttributes.addFlashAttribute("mensaje", "Película guardada exitosamente.");
        return "redirect:/admin/peliculas";
    }

    @GetMapping("/editar/{id}")
    public String mostrarFormularioEditar(@PathVariable Long id, Model model) {
        Pelicula pelicula = peliculaService.obtenerPeliculaPorId(id)
                .orElseThrow(() -> new IllegalArgumentException("ID de Película inválido:" + id));
        model.addAttribute("pelicula", pelicula);
        return "admin/peliculas/formulario";
    }

    @PostMapping("/eliminar/{id}")
    public String eliminarPelicula(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        peliculaService.eliminarPelicula(id);
        redirectAttributes.addFlashAttribute("mensaje", "Película eliminada exitosamente.");
        return "redirect:/admin/peliculas";
    }
}