package com.example.tonalli_backend.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ClassResponse {
    private Integer idClase;
    private String nombreClase;
    private Short grado;
    private String codigoClase;
    private Integer totalAlumnos; // Un extra útil para la tarjeta
}
