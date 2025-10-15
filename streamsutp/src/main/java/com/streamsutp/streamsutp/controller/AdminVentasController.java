package com.streamsutp.streamsutp.controller;

import com.streamsutp.streamsutp.model.Orden;
import com.streamsutp.streamsutp.model.EstadoOrden;
import com.streamsutp.streamsutp.service.OrdenService;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;
import java.util.Optional;

@Controller
// CAMBIO: Renombramos la ruta para evitar conflictos con la API REST.
@RequestMapping("/admin/ventas-ui") 
@PreAuthorize("hasRole('ADMIN')")
public class AdminVentasController {

    private final OrdenService ordenService;

    public AdminVentasController(OrdenService ordenService) {
        this.ordenService = ordenService;
    }

    @GetMapping
    public String listarOrdenes(Model model) {
        List<Orden> ordenes = ordenService.findAllOrdenes();
        model.addAttribute("ordenes", ordenes);
        return "admin/ventas/lista";
    }

    @GetMapping("/{id}")
    public String verDetalleOrden(@PathVariable Long id, Model model, RedirectAttributes redirectAttributes) {
        Optional<Orden> ordenOptional = ordenService.findOrdenById(id);
        if (ordenOptional.isPresent()) {
            model.addAttribute("orden", ordenOptional.get());
            model.addAttribute("estadosDisponibles", EstadoOrden.values());
            return "admin/ventas/detalle";
        } else {
            redirectAttributes.addFlashAttribute("error", "Orden no encontrada.");
            return "redirect:/admin/ventas-ui"; // Actualizamos el redirect
        }
    }

    @PostMapping("/actualizar-estado")
    public String actualizarEstadoOrden(@RequestParam Long idOrden,
                                        @RequestParam EstadoOrden nuevoEstado,
                                        RedirectAttributes redirectAttributes) {
        try {
            ordenService.actualizarEstadoOrden(idOrden, nuevoEstado);
            redirectAttributes.addFlashAttribute("mensaje", "Estado de la orden actualizado correctamente.");
        } catch (RuntimeException e) {
            redirectAttributes.addFlashAttribute("error", "Error al actualizar el estado: " + e.getMessage());
        }
        return "redirect:/admin/ventas-ui/" + idOrden; // Actualizamos el redirect
    }

    @PostMapping("/eliminar/{id}")
    public String eliminarOrden(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        try {
            ordenService.deleteOrden(id);
            redirectAttributes.addFlashAttribute("mensaje", "Orden eliminada correctamente.");
        } catch (RuntimeException e) {
            redirectAttributes.addFlashAttribute("error", "Error al eliminar la orden: " + e.getMessage());
        }
        return "redirect:/admin/ventas-ui"; // Actualizamos el redirect
    }
}