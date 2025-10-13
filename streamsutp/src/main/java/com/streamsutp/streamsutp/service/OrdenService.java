package com.streamsutp.streamsutp.service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.streamsutp.streamsutp.model.CarritoItem;
import com.streamsutp.streamsutp.model.DetalleOrden;
import com.streamsutp.streamsutp.model.EstadoOrden;
import com.streamsutp.streamsutp.model.Orden;
import com.streamsutp.streamsutp.model.Pelicula;
import com.streamsutp.streamsutp.model.TipoVenta;
import com.streamsutp.streamsutp.model.Usuario;
import com.streamsutp.streamsutp.repository.DetalleOrdenRepository;
import com.streamsutp.streamsutp.repository.OrdenRepository;
import com.streamsutp.streamsutp.repository.PeliculaRepository;

@Service
public class OrdenService {

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

            //if (pelicula.getStock() < item.getCantidad()) {
            //    throw new IllegalStateException("No hay suficiente stock para la película: " + pelicula.getTitulo());
            //}

            // Lógica para determinar el precio según el tipo de venta
            BigDecimal precioUnitario=pelicula.getPrecioComprar();
            
            TipoVenta tipoVenta = TipoVenta.valueOf(item.getTipo().toUpperCase());

            //try {
            //    tipoVenta = TipoVenta.valueOf(item.getTipo().toUpperCase());
            //} catch (IllegalArgumentException e) {
            //    throw new IllegalArgumentException("Tipo de venta no válido para la película " + item.getTitulo() + ": " + item.getTipo());
            //}

            //if (tipoVenta == TipoVenta.ALQUILER) {
            //    precioUnitario = pelicula.getPrecioAlquilar();
            //} else {
            //    precioUnitario = pelicula.getPrecioComprar();
            //}
            // Fin de la lógica de precio

            //pelicula.setStock(pelicula.getStock() - item.getCantidad());
            //peliculaRepository.save(pelicula);

            DetalleOrden detalle = new DetalleOrden(nuevaOrden, pelicula, item.getCantidad(), precioUnitario, tipoVenta);
            nuevaOrden.addDetalle(detalle);
            totalGeneral = totalGeneral.add(detalle.getSubtotal());
        }

        nuevaOrden.setTotalOrden(totalGeneral);
        return ordenRepository.save(nuevaOrden);
    }

    @Transactional
    public void deleteOrden(Long id) {
        Orden orden = ordenRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("No se encontró la orden para eliminar: " + id));

        //for (DetalleOrden detalle : orden.getDetalles()) {
        //    Pelicula pelicula = detalle.getPelicula();
        //    pelicula.setStock(pelicula.getStock() + detalle.getCantidad());
        //    peliculaRepository.save(pelicula);
        //}

        ordenRepository.delete(orden);
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
}