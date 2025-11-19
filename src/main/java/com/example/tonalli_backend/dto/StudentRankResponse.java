package com.example.tonalli_backend.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class StudentRankResponse {
    private Integer idAlumno;
    private String nombre;
    private String avatarUrl; // Base64
    private Integer estrellas;
}