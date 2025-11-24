package com.example.tonalli_backend.dto;

import lombok.Builder;
import lombok.Data;
import java.time.LocalDateTime;

@Data
@Builder
public class ActivityItem {
    private Integer idAsignacion;
    private Integer idActividad;
    private String titulo;
    private String descripcion;
    private String tema; // Nombre del Topic
    private LocalDateTime fechaLimite;
    // Puedes agregar 'calificacion' aquí si quieres mostrarla en las completadas
}