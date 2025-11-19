package com.example.tonalli_backend.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ClassActivityResponse {
    private Integer idActividad;
    private String titulo;
    private String descripcion;
    private String tema; // Nombre del topic
    private boolean asignada; // <-- El campo clave
}