package com.example.tonalli_backend.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class AssignmentResponse {
    private String mensaje;
    private Integer idAsignacion; // ID de la nueva asignación creada
}