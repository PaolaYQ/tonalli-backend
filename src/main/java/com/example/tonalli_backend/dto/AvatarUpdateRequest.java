package com.example.tonalli_backend.dto;

import lombok.Data;
import java.util.List;

@Data
public class AvatarUpdateRequest {
    // IDs de los ítems que el alumno decidió ponerse
    private List<Integer> itemsEquipadosIds;
    // La imagen final generada por el frontend (Base64)
    private String imagenBase64;
}