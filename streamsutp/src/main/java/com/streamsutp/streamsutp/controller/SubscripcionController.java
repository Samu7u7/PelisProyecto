package com.streamsutp.streamsutp.controller;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.streamsutp.streamsutp.model.Subscripcion;
import com.streamsutp.streamsutp.model.Usuario;
import com.streamsutp.streamsutp.service.SubscripcionService;
import com.streamsutp.streamsutp.service.UsuarioService;

@Controller
@RequestMapping("/suscripciones")
public class SubscripcionController {

    private final SubscripcionService subsService;
    private final UsuarioService usuarioService;

    public SubscripcionController(SubscripcionService subsService, UsuarioService usuarioService) {
        this.subsService = subsService;
        this.usuarioService = usuarioService;
    }

    // Página de suscripción para el usuario
    @GetMapping("/nuevo")
    public String mostrarFormularioSuscripcion(Model model) {
        model.addAttribute("subscripcion", new Subscripcion());
        return "usuario/formularioSuscripcion";
    }

    // Procesar nueva suscripción
    @PostMapping("/nuevo")
    public String crearSuscripcion(@ModelAttribute Subscripcion subs,
                                   Authentication auth,
                                   RedirectAttributes redirect) {
        String username = auth.getName();
        Usuario usuario = usuarioService.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));
        // Parámetros fijos o calculados en frontend: precio, fechas
        subsService.crearSubscripcion(
            usuario,
            subs.getPrecioPagado(),
            subs.getFechaInicio(),
            subs.getFechaFin()
        );
        redirect.addFlashAttribute("mensaje", "Suscripción creada correctamente");
        return "redirect:/perfil";
    }

    // Listar suscripciones del usuario
    @GetMapping("/historial")
    public String verHistorial(Model model, Authentication auth) {
        String username = auth.getName();
        Usuario usuario = usuarioService.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));
        model.addAttribute("historial", subsService.findByUsuario(usuario));
        return "usuario/historialSubscripciones";
    }

    // ======= Rutas de ADMIN para suscripciones =======

    @GetMapping("/admin/lista")
    @PreAuthorize("hasRole('ADMIN')")
    public String listarTodas(Model model) {
        model.addAttribute("subscripciones", subsService.findAll());
        return "admin/listaSubscripciones";
    }

    @PostMapping("/admin/cancelar/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public String cancelar(@PathVariable Long id, RedirectAttributes redirect) {
        subsService.cancelarSubscripcion(id);
        redirect.addFlashAttribute("mensaje", "Suscripción cancelada");
        return "redirect:/suscripciones/admin/lista";
    }
}
