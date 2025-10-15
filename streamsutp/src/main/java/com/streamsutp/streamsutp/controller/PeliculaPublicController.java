package com.streamsutp.streamsutp.controller;

import com.streamsutp.streamsutp.service.PeliculaService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@Controller
public class PeliculaPublicController {

    private final PeliculaService peliculaService;

    public PeliculaPublicController(PeliculaService peliculaService) {
        this.peliculaService = peliculaService;
    }

    @GetMapping("/pelicula/{id}")
    public String verDetalle(@PathVariable Long id, Model model) {
        var peliculaOpt = peliculaService.obtenerPeliculaPorId(id);
        if (peliculaOpt.isEmpty()) {
            return "redirect:/"; // redirige si no existe
        }
        model.addAttribute("pelicula", peliculaOpt.get());
        return "pelicula-detalle";
    }
}
