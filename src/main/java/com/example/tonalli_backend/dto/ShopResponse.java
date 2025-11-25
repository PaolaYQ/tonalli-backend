package com.example.tonalli_backend.dto;

import lombok.Builder;
import lombok.Data;
import java.util.List;
import java.util.Map;

@Data
@Builder
public class ShopResponse {
    private Integer estrellasDisponibles; // Saldo para gastar
    // Mapa: "Sombrero" -> [Gorra, Casco...], "Ropa" -> [Camisa...]
    private Map<String, List<ShopItemDTO>> itemsPorTipo; 
}