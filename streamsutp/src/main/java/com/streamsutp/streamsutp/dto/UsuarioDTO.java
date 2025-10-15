package com.streamsutp.streamsutp.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * DTO para transferir la información esencial de un Usuario.
 * Es un objeto simple, sin conexiones a la base de datos.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class UsuarioDTO {
    private Long id;
    private String username;
}