package com.example.tonalli_backend.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class QuestionDTO {
    private Integer idPregunta;
    private String enunciado;
    private String opcionA;
    private String opcionB;
    private String opcionC;
    private String opcionD;
    private String respuestaCorrecta;
}