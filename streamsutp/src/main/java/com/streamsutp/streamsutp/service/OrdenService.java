package com.streamsutp.streamsutp.service;

import com.streamsutp.streamsutp.dto.DetalleOrdenDTO;
import com.streamsutp.streamsutp.dto.OrdenDTO;
import com.streamsutp.streamsutp.dto.PeliculaDTO;
import com.streamsutp.streamsutp.dto.UsuarioDTO;
import com.streamsutp.streamsutp.model.*;
import com.streamsutp.streamsutp.repository.DetalleOrdenRepository;
import com.streamsutp.streamsutp.repository.OrdenRepository;
import com.streamsutp.streamsutp.repository.PeliculaRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class OrdenService {

    private static final Logger logger = LoggerFactory.getLogger(OrdenService.class);
    private final OrdenRepository ordenRepository;
    private final DetalleOrdenRepository detalleOrdenRepository;
    private final PeliculaService peliculaService;
    private final UsuarioService usuarioService;
    private final PeliculaRepository peliculaRepository;

    public OrdenService(OrdenRepository ordenRepository, DetalleOrdenRepository detalleOrdenRepository,
                        PeliculaService peliculaService, UsuarioService usuarioService,
                        PeliculaRepository peliculaRepository) {
        this.ordenRepository = ordenRepository;
        this.detalleOrdenRepository = detalleOrdenRepository;
        this.peliculaService = peliculaService;
        this.usuarioService = usuarioService;
        this.peliculaRepository = peliculaRepository;
    }

    // --- MÉTODOS ORIGINALES (se mantienen igual) ---
    @Transactional
    public Orden crearOrden(Long idUsuario, List<CarritoItem> itemsDelCarrito) {
        Usuario usuario = usuarioService.findUsuarioById(idUsuario)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado con ID: " + idUsuario));
        Orden nuevaOrden = new Orden();
        nuevaOrden.setUsuario(usuario);
        nuevaOrden.setFechaOrden(LocalDateTime.now());
        nuevaOrden.setEstadoOrden(EstadoOrden.PENDIENTE);
        BigDecimal totalGeneral = BigDecimal.ZERO;
        for (CarritoItem item : itemsDelCarrito) {
            Pelicula pelicula = peliculaService.obtenerPeliculaPorId(item.getPeliculaId())
                    .orElseThrow(() -> new RuntimeException("Película no encontrada con ID: " + item.getPeliculaId()));
            BigDecimal precioUnitario = pelicula.getPrecioComprar();
            TipoVenta tipoVenta = TipoVenta.valueOf(item.getTipo().toUpperCase());
            DetalleOrden detalle = new DetalleOrden(nuevaOrden, pelicula, item.getCantidad(), precioUnitario, tipoVenta);
            nuevaOrden.addDetalle(detalle);
            totalGeneral = totalGeneral.add(detalle.getSubtotal());
        }
        nuevaOrden.setTotalOrden(totalGeneral);
        return ordenRepository.save(nuevaOrden);
    }

    @Transactional
    public void deleteOrden(Long id) {
        ordenRepository.deleteById(id);
    }

    public List<Orden> findAllOrdenes() {
        return ordenRepository.findAll();
    }

    public Optional<Orden> findOrdenById(Long id) {
        return ordenRepository.findById(id);
    }

    @Transactional
    public Orden actualizarEstadoOrden(Long idOrden, EstadoOrden nuevoEstado) {
        Orden orden = ordenRepository.findById(idOrden)
                .orElseThrow(() -> new RuntimeException("Orden no encontrada con ID: " + idOrden));
        orden.setEstadoOrden(nuevoEstado);
        return ordenRepository.save(orden);
    }

    public List<Orden> findOrdenesByUsuario(Usuario usuario) {
        return ordenRepository.findByUsuario(usuario);
    }

    public List<Orden> findOrdenesByEstado(EstadoOrden estado) {
        return ordenRepository.findByEstadoOrden(estado);
    }

    // --- MÉTODOS PARA LA API REST (CON LÓGICA DE MAPEO INTEGRADA) ---

    @Transactional(readOnly = true)
    public List<OrdenDTO> findOrdenesByEstadoAsDTO(EstadoOrden estado) {
        logger.info("Buscando órdenes por estado: {}", estado);
        try {
            List<Orden> ordenes = ordenRepository.findByEstadoOrden(estado);
            logger.info("Se encontraron {} órdenes con estado {}", ordenes.size(), estado);
            return ordenes.stream()
                    .map(this::toOrdenDTO)
                    .collect(Collectors.toList());
        } catch (Exception e) {
            logger.error("Error al buscar órdenes por estado {}: {}", estado, e.getMessage());
            throw new RuntimeException("Error al buscar órdenes por estado: " + e.getMessage());
        }
    }

    @Transactional(readOnly = true)
    public List<OrdenDTO> findAllOrdenesAsDTO() {
        logger.info("Buscando todas las órdenes");
        try {
            List<Orden> ordenes = ordenRepository.findAll();
            logger.info("Se encontraron {} órdenes en total", ordenes.size());
            return ordenes.stream()
                    .map(this::toOrdenDTO)
                    .collect(Collectors.toList());
        } catch (Exception e) {
            logger.error("Error al buscar todas las órdenes: {}", e.getMessage());
            throw new RuntimeException("Error al buscar todas las órdenes: " + e.getMessage());
        }
    }

    @Transactional(readOnly = true)
    public Optional<OrdenDTO> findOrdenByIdAsDTO(Long id) {
        return ordenRepository.findById(id).map(this::toOrdenDTO);
    }

    @Transactional
    public OrdenDTO actualizarEstadoOrdenAsDTO(Long idOrden, EstadoOrden nuevoEstado) {
        Orden orden = ordenRepository.findById(idOrden)
                .orElseThrow(() -> new RuntimeException("Orden no encontrada con ID: " + idOrden));
        orden.setEstadoOrden(nuevoEstado);
        Orden ordenGuardada = ordenRepository.save(orden);
        return toOrdenDTO(ordenGuardada);
    }

    // --- LÓGICA DE MAPEO (antes en OrdenMapper) ---

    private OrdenDTO toOrdenDTO(Orden orden) {
        // La transacción está activa aquí, por lo que podemos acceder a los campos lazy.
        return new OrdenDTO(
            orden.getId(),
            toUsuarioDTO(orden.getUsuario()),
            orden.getFechaOrden(),
            orden.getTotalOrden(),
            orden.getEstadoOrden(),
            orden.getDetalles().stream()
                .map(this::toDetalleOrdenDTO)
                .collect(Collectors.toList())
        );
    }

    private DetalleOrdenDTO toDetalleOrdenDTO(DetalleOrden detalle) {
        return new DetalleOrdenDTO(
            detalle.getId(),
            toPeliculaDTO(detalle.getPelicula()),
            detalle.getCantidad(),
            detalle.getPrecioUnitario(),
            detalle.getTipoVenta(),
            detalle.getSubtotal()
        );
    }

    private UsuarioDTO toUsuarioDTO(Usuario usuario) {
        return new UsuarioDTO(
            usuario.getId(),
            usuario.getUsername()
        );
    }

    private PeliculaDTO toPeliculaDTO(Pelicula pelicula) {
        return new PeliculaDTO(
            pelicula.getId(),
            pelicula.getTitulo()
        );
    }
}