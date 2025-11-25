package com.example.tonalli_backend.dto;

import lombok.Builder;
import lombok.Data;
import java.util.List;

@Data
@Builder
public class StudentProfileDetailResponse {
    private String nombre;
    private String correo;
    private String avatarUrl; // Base64
    private Integer estrellasTotales; // Histórico
    private Integer estrellasDisponibles; // Dinero actual
    private List<MedalResponse> medallas;
}