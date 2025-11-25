package com.example.tonalli_backend.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ShopItemDTO {
    private Integer idItem;
    private String nombre;
    private Integer precio;
    private String iconoUrl;
    private boolean adquirido; // True si el alumno ya lo compró
    private boolean enUso; // True si el alumno lo trae puesto
}