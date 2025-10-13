package com.streamsutp.streamsutp.controller;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.streamsutp.streamsutp.model.CarritoItem;
import com.streamsutp.streamsutp.model.Pelicula;
import com.streamsutp.streamsutp.model.Usuario;
import com.streamsutp.streamsutp.service.OrdenService;
import com.streamsutp.streamsutp.service.PeliculaService;
import com.streamsutp.streamsutp.service.UsuarioService;

import jakarta.servlet.http.HttpSession; 

@Controller
@RequestMapping("/carrito")
public class CarritoController {
    private static final Logger logger = LoggerFactory.getLogger(CarritoController.class);

    private final OrdenService ordenService;
    private final PeliculaService peliculaService;
    private final UsuarioService usuarioService;

    public CarritoController(OrdenService ordenService, PeliculaService peliculaService, UsuarioService usuarioService) {
        this.ordenService = ordenService;
        this.peliculaService = peliculaService;
        this.usuarioService = usuarioService;
    }

    // --- Métodos del Carrito ---

    @PostMapping("/agregar")
    public String agregarAlCarrito(@RequestParam Long peliculaId,
                                   @RequestParam String titulo,
                                   @RequestParam(defaultValue = "comprar") String tipo,
                                   @RequestParam String imagen,
                                   @RequestParam(defaultValue = "1") int cantidad,
                                   HttpSession session,
                                   RedirectAttributes redirectAttributes) {

        if (!tipo.equalsIgnoreCase("comprar") ) {
            redirectAttributes.addFlashAttribute("error", "Sólo se permite compra de películas.");
            return "redirect:/";
        }

        Optional<Pelicula> peliculaOpt = peliculaService.obtenerPeliculaPorId(peliculaId);
        if (peliculaOpt.isEmpty()) {
            redirectAttributes.addFlashAttribute("error", "Película no encontrada.");
            return "redirect:/";
        }
        Pelicula pelicula = peliculaOpt.get();

        BigDecimal precioReal = pelicula.getPrecioComprar();

        //BigDecimal precioReal;
        //if (tipo.equalsIgnoreCase("alquiler")) {
        //    precioReal = pelicula.getPrecioAlquilar();
        //} else if (tipo.equalsIgnoreCase("comprar")) {
        //    precioReal = pelicula.getPrecioComprar();
        //} else {
        //    redirectAttributes.addFlashAttribute("error", "Tipo de venta no especificado o inválido.");
        //    return "redirect:/";
       // }


        List<CarritoItem> carrito = (List<CarritoItem>) session.getAttribute("carrito");

        if (carrito == null) {
            carrito = new ArrayList<>();
        }

        carrito.add(new CarritoItem(peliculaId, titulo, tipo, precioReal, imagen, cantidad));
        session.setAttribute("carrito", carrito);

        String accion = tipo.equalsIgnoreCase("comprar") ? "comprada" : "alquilada";
        String mensaje = "¡Película '" + titulo + "' " + accion + " correctamente! Se ha añadido al carrito.";
        redirectAttributes.addFlashAttribute("mensaje", mensaje);

        return "redirect:/";
    }

    @GetMapping("")
    public String mostrarCarrito(Model model, HttpSession session, RedirectAttributes redirectAttributes, Authentication authentication) {
        if (authentication == null || !authentication.isAuthenticated()) {
            redirectAttributes.addFlashAttribute("error", "Debes iniciar sesión para ver tu carrito.");
            return "redirect:/ingreso";
        }

        List<CarritoItem> carrito = (List<CarritoItem>) session.getAttribute("carrito");
        if (carrito == null) {
            carrito = new ArrayList<>();
        }

        BigDecimal total = carrito.stream()
                                     .map(CarritoItem::getPrecio)
                                     .reduce(BigDecimal.ZERO, BigDecimal::add);

        model.addAttribute("carrito", carrito);
        model.addAttribute("total", total);
        return "carrito";
    }

    @PostMapping("/eliminar")
    public String eliminarDelCarrito(@RequestParam int index, HttpSession session, RedirectAttributes redirectAttributes, Authentication authentication) {
        if (authentication == null || !authentication.isAuthenticated()) {
            redirectAttributes.addFlashAttribute("error", "Debes iniciar sesión para modificar tu carrito.");
            return "redirect:/ingreso";
        }

        List<CarritoItem> carrito = (List<CarritoItem>) session.getAttribute("carrito");

        if (carrito != null && index >= 0 && index < carrito.size()) {
            carrito.remove(index);
            session.setAttribute("carrito", carrito);
            redirectAttributes.addFlashAttribute("mensaje", "Elemento eliminado del carrito.");
        } else {
            redirectAttributes.addFlashAttribute("error", "No se pudo eliminar el elemento.");
        }

        return "redirect:/carrito";
    }

    @PostMapping("/vaciar")
    public String vaciarCarrito(HttpSession session, RedirectAttributes redirectAttributes, Authentication authentication) {
        if (authentication == null || !authentication.isAuthenticated()) {
            redirectAttributes.addFlashAttribute("error", "Debes iniciar sesión para vaciar tu carrito.");
            return "redirect:/ingreso";
        }

        session.removeAttribute("carrito");
        redirectAttributes.addFlashAttribute("mensaje", "El carrito ha sido vaciado.");
        return "redirect:/carrito";
    }

    // --- NUEVO MÉTODO: Procesar la compra y crear la orden ---
    @PostMapping("/procesar-compra")
    public String procesarCompra(HttpSession session, RedirectAttributes redirectAttributes, Authentication authentication) {
        // 1. Verificar autenticación
        if (authentication == null || !authentication.isAuthenticated()) {
            redirectAttributes.addFlashAttribute("error", "Debes iniciar sesión para completar la compra.");
            return "redirect:/ingreso";
        }

        // 2. Obtener el usuario logueado de Spring Security
        String username = authentication.getName();
        Usuario usuarioLogueado = usuarioService.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("Usuario autenticado no encontrado en la base de datos: " + username));

        // 3. Obtener los items del carrito de la sesión
        List<CarritoItem> carrito = (List<CarritoItem>) session.getAttribute("carrito");

        if (carrito == null || carrito.isEmpty()) {
            redirectAttributes.addFlashAttribute("error", "Tu carrito está vacío. Agrega películas antes de comprar.");
            return "redirect:/carrito";
        }

        try {
            // 4. Llamar al servicio de órdenes para crear la orden
            ordenService.crearOrden(usuarioLogueado.getId(), carrito);

            // 5. Vaciar el carrito después de una compra exitosa
            session.removeAttribute("carrito");

            redirectAttributes.addFlashAttribute("mensaje", "¡Tu compra se ha procesado exitosamente! Revisa el historial de tus compras.");
            return "redirect:/confirmacion-compra"; // Redirige a una página de confirmación
        } catch (RuntimeException e) {
            // Manejo de errores durante la creación de la orden (ej. película no encontrada)
            logger.error("Error al procesar la compra: " + e.getMessage(), e); // Log del error detallado
            redirectAttributes.addFlashAttribute("error", "Hubo un error al procesar tu compra: " + e.getMessage());
            return "redirect:/carrito"; // Vuelve al carrito con un mensaje de error
        }
    }
}