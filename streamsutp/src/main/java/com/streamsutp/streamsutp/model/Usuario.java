package com.streamsutp.streamsutp.model;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity; // Importa para @EqualsAndHashCode.Exclude
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue; // Importa para @ToString.Exclude
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table; // Para inicializar la lista de ordenes
import jakarta.persistence.Transient;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;
// --------------------------------------------------------

@Entity
@Table(name="usuarios")
@Data // Anotación de Lombok para generar getters, setters, toString, equals y hashCode
@NoArgsConstructor // Anotación de Lombok para generar un constructor sin argumentos
@AllArgsConstructor // Anotación de Lombok para generar un constructor con todos los argumentos
public class Usuario implements UserDetails { // <--- ¡AQUÍ ES DONDE IMPLEMENTA UserDetails!

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "Los apellidos son obligatorios")
    private String apellidos;

    @NotBlank(message = "Los nombres son obligatorios")
    private String nombres;

    @Column(name="username", unique = true) // Añadido 'unique = true' si no lo tenías antes
    @NotBlank(message = "El usuario es obligatorio")
    private String username;

    @NotBlank(message = "La contraseña es obligatoria")
    @Size(min = 8, message = "La contraseña debe tener al menos 8 caracteres")
    private String password;

    @Transient // Indica que este campo no se mapeará a una columna en la base de datos
    private String repetirPassword;

    @NotBlank(message = "El correo es obligatorio")
    @Email(message = "Debe ser un correo válido")
    @Pattern(regexp = "^[\\w.-]+@gmail\\.com$", message = "El correo debe ser un gmail válido")
    @Column(unique = true) // Añadido 'unique = true' para el email si no lo tenías
    private String email;

    //@Transient // Indica que este campo no se mapeará a una columna en la base de datos
    //private String repetirPassword;

    @Column(nullable = false) // 'rol' no puede ser nulo, asumimos que siempre tendrá un valor
    private String rol;

    // --- RELACIÓN ONE-TO-MANY CON ORDENES ---
    // Un usuario puede tener muchas órdenes.
    // 'mappedBy' indica el nombre del campo en la clase 'Orden' que es el dueño de la relación (el lado ManyToOne).
    // 'cascade = CascadeType.ALL' significa que si se elimina un usuario, se eliminan todas sus órdenes.
    // 'orphanRemoval = true' significa que si una orden se desvincula de un usuario, se elimina de la BD.
    @OneToMany(mappedBy = "usuario", cascade = CascadeType.ALL, orphanRemoval = true)
    @ToString.Exclude // Evita recursión infinita cuando Lombok genera toString()
    @EqualsAndHashCode.Exclude // Evita recursión infinita cuando Lombok genera equals() y hashCode()
    private List<Orden> ordenes = new ArrayList<>(); // Se inicializa para evitar NullPointerExceptions

    // --- IMPLEMENTACIÓN DE LOS MÉTODOS DE UserDetails ---
    // Lombok (@Data) ya provee los getters para 'username' y 'password'.
    // Los demás métodos deben ser implementados explícitamente.

    // Relaciones con subscripciones
    @OneToMany(mappedBy = "usuario", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    private List<Subscripcion> subscripciones = new ArrayList<>();


    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        // Convierte el String 'rol' del usuario a una colección de GrantedAuthority.
        // Spring Security espera que los roles comiencen con "ROLE_" (ej. "ROLE_USER", "ROLE_ADMIN").
        return List.of(new SimpleGrantedAuthority("ROLE_" + rol));
    }

    // @Override public String getPassword() { return this.password; } // Lombok @Data ya lo genera
    // @Override public String getUsername() { return this.username; } // Lombok @Data ya lo genera

    @Override
    public boolean isAccountNonExpired() {
        // Indica si la cuenta del usuario no ha expirado. Para la mayoría de apps, es siempre true.
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        // Indica si la cuenta del usuario no está bloqueada. Para la mayoría de apps, es siempre true.
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        // Indica si las credenciales (contraseña) del usuario no han expirado. Para la mayoría de apps, es siempre true.
        return true;
    }

    @Override
    public boolean isEnabled() {
        // Indica si el usuario está habilitado. Para la mayoría de apps, es siempre true.
        return true;
    }

    // --- Método de utilidad para añadir órdenes a la lista del usuario (opcional pero recomendado) ---
    // Este método ayuda a mantener la bidireccionalidad de la relación.
    public void addOrden(Orden orden) {
        this.ordenes.add(orden);
        orden.setUsuario(this); // Asegura que la orden también conozca a su usuario
    }

    // Ayuda a mantener la bidireccionalidad con Subscripcion
    public void addSubscripcion(Subscripcion subscripcion) {
        this.subscripciones.add(subscripcion);
        subscripcion.setUsuario(this);
    }
}