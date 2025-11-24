package com.example.tonalli_backend.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class SubmitAttemptResponse {
    private Integer estrellasGanadas; // Estrellas ganadas en ESTE intento
    private Integer estrellasTotalesActividad; // Acumulado total en esta actividad (máx 3)
    private boolean medallaGanada; // ¿Ganó medalla nueva?
    private boolean actividadCompletada; // ¿Llegó al máximo de 3 estrellas?
    private String mensaje;
}