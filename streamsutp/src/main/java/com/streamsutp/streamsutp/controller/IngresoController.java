package com.streamsutp.streamsutp.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.ui.Model;

@Controller
public class IngresoController {

    @GetMapping("/ingreso")
    public String mostrarFormularioIngreso(@RequestParam(value = "error", required = false) String error,
                                          @RequestParam(value = "logout", required = false) String logout,
                                          Model model) {
        if (error != null) {
            model.addAttribute("loginError", true); // Flag para tu Thymeleaf
            model.addAttribute("errorMessage", "Nombre de usuario o contraseña incorrectos.");
        }
        if (logout != null) {
            model.addAttribute("logoutSuccess", true); // Flag para tu Thymeleaf
            model.addAttribute("successMessage", "Has cerrado sesión correctamente.");
        }
        return "ingreso";
    }
}