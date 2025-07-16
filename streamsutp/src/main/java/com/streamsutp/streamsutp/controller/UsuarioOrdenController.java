package com.streamsutp.streamsutp.controller;

import com.streamsutp.streamsutp.model.Orden;
import com.streamsutp.streamsutp.model.Usuario;
import com.streamsutp.streamsutp.service.OrdenService;
import com.streamsutp.streamsutp.service.UsuarioService;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.security.access.prepost.PreAuthorize;

import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping
public class UsuarioOrdenController {

    private final OrdenService ordenService;
    private final UsuarioService usuarioService;

    public UsuarioOrdenController(OrdenService ordenService, UsuarioService usuarioService) {
        this.ordenService = ordenService;
        this.usuarioService = usuarioService;
    }

    @GetMapping("/usuario/historial_ordenes")
    @PreAuthorize("isAuthenticated()")
    public String verHistorialOrdenes(Model model, Authentication authentication) {
        // Obtener el 'username' de Spring Security (que es 'osmar123' en tu caso)
        String username = ((UserDetails) authentication.getPrincipal()).getUsername();

        // CAMBIO CLAVE: Buscar el usuario por su username
        Optional<Usuario> usuarioOptional = usuarioService.findByUsername(username); // <--- CAMBIO AQUÍ

        if (usuarioOptional.isPresent()) {
            Usuario usuario = usuarioOptional.get();
            List<Orden> ordenes = ordenService.findOrdenesByUsuario(usuario);
            model.addAttribute("ordenes", ordenes);
            return "usuario/historial_ordenes";
        } else {
            model.addAttribute("error", "No se pudo encontrar la información del usuario.");
            return "redirect:/";
        }
    }

    @GetMapping("/usuario/historial_ordenes/{id}")
    @PreAuthorize("isAuthenticated()")
    public String verDetalleOrdenUsuario(@PathVariable Long id, Model model, Authentication authentication) {
        String username = ((UserDetails) authentication.getPrincipal()).getUsername(); // Sigue siendo username
        Optional<Usuario> usuarioOptional = usuarioService.findByUsername(username); // <--- CAMBIO AQUÍ

        if (usuarioOptional.isPresent()) {
            Usuario usuario = usuarioOptional.get();
            Optional<Orden> ordenOptional = ordenService.findOrdenById(id);

            if (ordenOptional.isPresent()) {
                Orden orden = ordenOptional.get();
                if (orden.getUsuario().getId().equals(usuario.getId())) {
                    model.addAttribute("orden", orden);
                    return "usuario/detalle_orden_usuario";
                } else {
                    model.addAttribute("error", "No tienes permiso para ver esta orden.");
                    return "redirect:/usuario/historial_ordenes";
                }
            } else {
                model.addAttribute("error", "Orden no encontrada.");
                return "redirect:/usuario/historial_ordenes";
            }
        } else {
            model.addAttribute("error", "No se pudo encontrar la información del usuario.");
            return "redirect:/";
        }
    }
}