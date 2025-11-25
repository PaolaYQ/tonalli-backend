package com.example.tonalli_backend.dto;

import lombok.Builder;
import lombok.Data;
import java.time.LocalDateTime;

@Data
@Builder
public class MedalResponse {
    private Integer idMedalla;
    private String nombre;
    private String descripcion;
    private String iconoUrl;
    private String tema;
    private LocalDateTime fechaObtenida;
}