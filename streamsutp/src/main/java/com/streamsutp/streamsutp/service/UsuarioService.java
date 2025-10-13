package com.streamsutp.streamsutp.service;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.streamsutp.streamsutp.model.Usuario;
import com.streamsutp.streamsutp.repository.UserRepository;

@Service
public class UsuarioService implements UserDetailsService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public UsuarioService(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Usuario usuario = userRepository.findByUsername(username)
                               .orElseThrow(() -> new UsernameNotFoundException("Usuario no encontrado: " + username));

        List<GrantedAuthority> authorities = Collections.singletonList(new SimpleGrantedAuthority("ROLE_" + usuario.getRol()));

        return new User(usuario.getUsername(), usuario.getPassword(), authorities);
    }

    public boolean registrar(Usuario usuario) {
        if (userRepository.findByUsername(usuario.getUsername()).isPresent() ||
            userRepository.findByEmail(usuario.getEmail()).isPresent()) {
            return false; // El usuario o email ya existe
        }

        // ¡Esta línea ya está encriptando para el registro!
        usuario.setPassword(passwordEncoder.encode(usuario.getPassword()));

        if (usuario.getRol() == null || usuario.getRol().isEmpty()) {
            usuario.setRol("USER");
        }

        userRepository.save(usuario);
        return true;
    }

    public Optional<Usuario> findByUsername(String username) {
        return userRepository.findByUsername(username);
    }
   // --- NUEVO MÉTODO A AGREGAR ---
    public Optional<Usuario> findUsuarioByEmail(String email) {
        return userRepository.findByEmail(email);
    }
    // --- FIN NUEVO MÉTODO ---

    public List<Usuario> findAllUsuarios() {
        return userRepository.findAll();
    }

    public Optional<Usuario> findUsuarioById(Long id) {
        return userRepository.findById(id);
    }
    // --- CAMBIOS EN ESTE MÉTODO: saveUsuario ---
    public Usuario saveUsuario(Usuario usuario) {
        // Si el usuario tiene un ID, significa que es una actualización
        if (usuario.getId() != null) {
            // Buscamos el usuario existente en la BD para obtener su contraseña actual si no se cambió
            Optional<Usuario> existingUserOpt = userRepository.findById(usuario.getId());
            if (existingUserOpt.isPresent()) {
                Usuario existingUser = existingUserOpt.get();

                // Si se proporcionó una nueva contraseña en el formulario (no está vacía), encriptarla
                if (usuario.getPassword() != null && !usuario.getPassword().isEmpty()) {
                    usuario.setPassword(passwordEncoder.encode(usuario.getPassword()));
                } else {
                    // Si la contraseña no se proporcionó (se dejó vacía en el formulario de edición),
                    // mantener la contraseña existente de la BD.
                    usuario.setPassword(existingUser.getPassword());
                }
            } else {
                // Caso raro: se proporcionó un ID pero el usuario no existe.
                // En este escenario, tratamos como un nuevo usuario y encriptamos si hay contraseña.
                if (usuario.getPassword() != null && !usuario.getPassword().isEmpty()) {
                    usuario.setPassword(passwordEncoder.encode(usuario.getPassword()));
                }
            }
        } else {
            // Si no tiene ID, es un usuario nuevo (creado desde el admin)
            // Siempre encriptar la contraseña para un nuevo usuario.
            if (usuario.getPassword() != null && !usuario.getPassword().isEmpty()) {
                usuario.setPassword(passwordEncoder.encode(usuario.getPassword()));
            } else {
                // Si es un nuevo usuario y no se proporciona contraseña, manejar el error o asignar una por defecto.
                // Para simplificar, aquí se podría lanzar una excepción si la contraseña es obligatoria.
                // Por ahora, asumimos que siempre se envía una contraseña para un usuario nuevo.
            }
        }

        // Asegurarse de que el rol se establezca si es nulo (para nuevos usuarios o si el admin no lo asigna)
        if (usuario.getRol() == null || usuario.getRol().isEmpty()) {
            usuario.setRol("USER");
        }

        return userRepository.save(usuario);
    }
    // --- FIN DE CAMBIOS EN saveUsuario ---

    public void deleteUsuario(Long id) {
        userRepository.deleteById(id);
    }

    //usuarios(todos) con sub activa
    public List<Usuario> findSubscribedUsers() {
        return userRepository.findUsersWithActiveSubscription(LocalDateTime.now());
    }
}