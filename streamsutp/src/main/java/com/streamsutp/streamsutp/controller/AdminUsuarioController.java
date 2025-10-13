package com.streamsutp.streamsutp.controller;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.streamsutp.streamsutp.model.Usuario;
import com.streamsutp.streamsutp.service.UsuarioService;

@Controller
@RequestMapping("/admin/usuarios")
public class AdminUsuarioController {

    private final UsuarioService usuarioService;

    public AdminUsuarioController(UsuarioService usuarioService) {
        this.usuarioService = usuarioService;
    }

    @GetMapping("/lista")
    public String listarUsuarios(Model model) {
        model.addAttribute("usuarios", usuarioService.findAllUsuarios());
        return "admin/listaUsuarios";
    }

    //para las sub
    @GetMapping("/suscritos")
    public String listarUsuariosSuscritos(Model model) {
        List<Usuario> suscritos = usuarioService.findSubscribedUsers();
        model.addAttribute("usuariosSuscritos", suscritos);
        return "admin/usuariosSuscritos";
    }

    @GetMapping("/nuevo")
    public String mostrarFormularioNuevoUsuario(Model model) {
        model.addAttribute("usuario", new Usuario());
        return "admin/formularioUsuario";
    }

    @GetMapping("/editar/{id}")
    public String mostrarFormularioEditarUsuario(@PathVariable Long id, Model model, RedirectAttributes redirectAttributes) {
        Optional<Usuario> usuarioOpt = usuarioService.findUsuarioById(id);
        if (usuarioOpt.isPresent()) {
            model.addAttribute("usuario", usuarioOpt.get());
            return "admin/formularioUsuario";
        } else {
            redirectAttributes.addFlashAttribute("error", "Usuario no encontrado.");
            return "redirect:/admin/usuarios/lista";
        }
    }

    @PostMapping("/guardar")
    public String guardarUsuario(@ModelAttribute Usuario usuario, RedirectAttributes redirectAttributes) {
        usuarioService.saveUsuario(usuario);
        redirectAttributes.addFlashAttribute("mensaje", "Usuario guardado exitosamente!");
        return "redirect:/admin/usuarios/lista";
    }

    @PostMapping("/eliminar/{id}")
    public String eliminarUsuario(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        usuarioService.deleteUsuario(id);
        redirectAttributes.addFlashAttribute("mensaje", "Usuario eliminado exitosamente!");
        return "redirect:/admin/usuarios/lista";
    }
}