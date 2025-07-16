package com.streamsutp.streamsutp.controller;

import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import com.streamsutp.streamsutp.model.Usuario;
import com.streamsutp.streamsutp.service.UsuarioService;
import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

@Controller
public class RegistroController {

    private final UsuarioService usuarioService;

    public RegistroController(UsuarioService usuarioService) {
        this.usuarioService = usuarioService;
    }

    @GetMapping("/registro")
    public String mostrarFormulario(Model model) {
        model.addAttribute("usuario", new Usuario());
        return "registro";
    }

    @PostMapping("/registro")
    public String procesarFormulario(@Valid @ModelAttribute Usuario usuario, BindingResult result, Model model, RedirectAttributes redirectAttributes) {
        // Validar que las contraseñas coincidan
        if (!usuario.getPassword().equals(usuario.getRepetirPassword())) {
            model.addAttribute("mensajeRepetirPassword", "Las contraseñas no coinciden.");
            return "registro";
        }
        // Validaciones de anotaciones
        if (result.hasErrors()) {
            return "registro";
        }
        // Validar que el usuario no exista
        if (!usuarioService.registrar(usuario)) {
            model.addAttribute("mensaje", "El usuario o email ya existe.");
            return "registro";
        }
        redirectAttributes.addFlashAttribute("mensaje", "¡Gracias por registrarte, " + usuario.getNombres() + "! Por favor inicia sesión :)"); // ¡Añade esta línea!
        return "redirect:/ingreso";
    }
}