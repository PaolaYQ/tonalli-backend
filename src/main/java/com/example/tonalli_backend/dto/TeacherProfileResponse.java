package com.example.tonalli_backend.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class TeacherProfileResponse {
    private String nombre;

    private String avatarUrl;
}