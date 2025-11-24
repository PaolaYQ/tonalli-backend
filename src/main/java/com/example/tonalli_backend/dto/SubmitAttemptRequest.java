package com.example.tonalli_backend.dto;

import lombok.Data;
import java.math.BigDecimal;

@Data
public class SubmitAttemptRequest {
    private Integer idAsignacion;
    private BigDecimal resultadoPorcentaje; // 0 a 100
    private Integer duracionSegundos;
}