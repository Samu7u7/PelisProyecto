package com.streamsutp.streamsutp.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * DTO para la información esencial de una Película.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class PeliculaDTO {
    private long id;
    private String titulo;
}