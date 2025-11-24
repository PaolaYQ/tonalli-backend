package com.example.tonalli_backend.dto;

import lombok.Data;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDateTime;

@Data
public class ActivityAssignmentRequest {
    @NotNull
    private Integer idActividad;     // ID de la Actividad a asignar
    @NotNull
    private String codigoClase;      // Código de la clase destino
    @NotNull
    private LocalDateTime fechaLimite; // Fecha y hora límite de entrega
}