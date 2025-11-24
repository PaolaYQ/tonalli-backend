package com.example.tonalli_backend.dto;

import lombok.Builder;
import lombok.Data;
import java.util.List;

@Data
@Builder
public class ActivitiesDashboardResponse {
    private List<ActivityItem> pendientes;
    private List<ActivityItem> vencidas;
    private List<ActivityItem> completadas;
}