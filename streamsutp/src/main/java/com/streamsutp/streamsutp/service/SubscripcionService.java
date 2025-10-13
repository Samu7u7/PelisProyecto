package com.streamsutp.streamsutp.service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.streamsutp.streamsutp.model.Subscripcion;
import com.streamsutp.streamsutp.model.Usuario;
import com.streamsutp.streamsutp.repository.SubscripcionRepository;

@Service
public class SubscripcionService {

    private final SubscripcionRepository subscripcionRepository;

    public SubscripcionService(SubscripcionRepository subscripcionRepository) {
        this.subscripcionRepository = subscripcionRepository;
    }

    @Transactional
    public Subscripcion crearSubscripcion(Usuario usuario, BigDecimal precio, 
                                        LocalDateTime fechaInicio, LocalDateTime fechaFin) {
        Subscripcion subscripcion = new Subscripcion();
        subscripcion.setUsuario(usuario);
        subscripcion.setPrecioPagado(precio);
        subscripcion.setFechaInicio(fechaInicio);
        subscripcion.setFechaFin(fechaFin);
        subscripcion.setStatus(Subscripcion.Status.ACTIVE);
        
        return subscripcionRepository.save(subscripcion);
    }

    public List<Subscripcion> findByUsuario(Usuario usuario) {
        return subscripcionRepository.findByUsuario(usuario);
    }

    public Optional<Subscripcion> findActiveSubscription(Usuario usuario) {
        List<Subscripcion> activas = subscripcionRepository
            .findByUsuarioAndStatus(usuario, Subscripcion.Status.ACTIVE);
        return activas.stream().findFirst();
    }

    public List<Subscripcion> findAll() {
        return subscripcionRepository.findAll();
    }

    @Transactional
    public void cancelarSubscripcion(Long subscripcionId) {
        Subscripcion subscripcion = subscripcionRepository.findById(subscripcionId)
            .orElseThrow(() -> new RuntimeException("Suscripción no encontrada"));
        subscripcion.setStatus(Subscripcion.Status.CANCELED);
        subscripcionRepository.save(subscripcion);
    }
}
