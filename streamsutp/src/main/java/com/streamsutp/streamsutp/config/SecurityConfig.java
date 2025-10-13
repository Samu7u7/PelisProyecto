package com.streamsutp.streamsutp.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;


@Configuration
@EnableWebSecurity
public class SecurityConfig {

    // Este Bean es crucial para ignorar los recursos estáticos
    @Bean
    public WebSecurityCustomizer webSecurityCustomizer() {
        return (web) -> web.ignoring().requestMatchers(
                "/css/**",
                "/js/**",
                "/imagenes/**", // Ya corregido
                "/webjars/**",
                "/favicon.ico"
        );
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
            .authorizeHttpRequests(authorize -> authorize
                // Rutas de acceso público (cualquiera, incluso no autenticado)
                .requestMatchers("/ingreso", "/registro", "/").permitAll()

                // --- ¡CAMBIO AQUÍ! Permitir acceso a /carrito/** para usuarios autenticados ---
                // Para todas las URLs bajo /carrito/, asegúrate de que el usuario esté autenticado.
                // Esto hará que Spring Security permita el acceso si la sesión es válida.
                .requestMatchers("/carrito/**").authenticated()

                // Reglas específicas de rol
                .requestMatchers("/admin/**").hasRole("ADMIN")

                // Cualquier otra solicitud requiere autenticación (ya está bien)
                .anyRequest().authenticated()
            )
            .formLogin(form -> form
                .loginPage("/ingreso") // Tu página de login personalizada
                .defaultSuccessUrl("/", true) //fuerza a que todos vayan siempre al /
                .permitAll()
            )
            .logout(logout -> logout
                .permitAll());
            
            // Agrega esto para deshabilitar CSRF para pruebas o si tu HTML no lo maneja
            // Sin embargo, para producción, deberías asegurarte de incluir el token CSRF en tus formularios POST
            // .csrf(csrf -> csrf.disable()); // ESTO ES SOLO PARA PRUEBAS RÁPIDAS. LEE LA NOTA IMPORTANTE ABAJO.

        return http.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}