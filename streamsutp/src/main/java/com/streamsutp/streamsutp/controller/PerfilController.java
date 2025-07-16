package com.streamsutp.streamsutp.controller;

import com.streamsutp.streamsutp.model.Usuario;
import com.streamsutp.streamsutp.service.UsuarioService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import java.security.Principal;
import java.util.Optional; // Importar Optional

@Controller
public class PerfilController {

    private final UsuarioService usuarioService;

    public PerfilController(UsuarioService usuarioService) {
        this.usuarioService = usuarioService;
    }

    @GetMapping("/perfil")
    public String mostrarPerfil(Model model, Principal principal) {
        // Spring Security nos da el 'Principal' que contiene el username.
        String username = principal.getName();
        
        Optional<Usuario> usuarioOpt = usuarioService.findByUsername(username);
        
        if (usuarioOpt.isPresent()) {
            model.addAttribute("usuario", usuarioOpt.get());
        } else {
            // Esto no debería pasar si el usuario está autenticado, pero es un buen fallback.
            return "redirect:/ingreso?error=true";
        }
        return "perfil";
    }
}