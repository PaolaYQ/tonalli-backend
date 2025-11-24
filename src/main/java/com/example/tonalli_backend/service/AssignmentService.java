package com.example.tonalli_backend.service;

import com.example.tonalli_backend.dto.ActivityAssignmentRequest;
import com.example.tonalli_backend.dto.AssignmentResponse;
import com.example.tonalli_backend.entity.Actividad;
import com.example.tonalli_backend.entity.ActividadAsignada;
import com.example.tonalli_backend.entity.Clase;
import com.example.tonalli_backend.repository.ActividadAsignadaRepository;
import com.example.tonalli_backend.repository.ActividadRepository; // ¡Asumido!
import com.example.tonalli_backend.repository.ClaseRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class AssignmentService {

    private final ActividadAsignadaRepository asignacionRepository;
    private final ClaseRepository claseRepository;
    private final ActividadRepository actividadRepository; // ASUMIDO: Repositorio para la Actividad base

    @Transactional
    public AssignmentResponse createAssignment(ActivityAssignmentRequest request) {

        // 1. Obtener la clase por el código
        Clase clase = claseRepository.findByCodigoClase(request.getCodigoClase())
                .orElseThrow(() -> new IllegalArgumentException("Código de clase inválido o no existe."));

        // 2. Obtener la Actividad por ID
        Actividad actividad = actividadRepository.findById(request.getIdActividad())
                .orElseThrow(() -> new IllegalArgumentException(
                        "Actividad con ID " + request.getIdActividad() + " no encontrada."));

        // 3. Crear el objeto de asignación
        ActividadAsignada asignacion = ActividadAsignada.builder()
                .clase(clase)
                .actividad(actividad)
                .fechaLimite(request.getFechaLimite())
                .build();

        // 4. Guardar en la base de datos
        ActividadAsignada asignacionGuardada = asignacionRepository.save(asignacion);

        // 5. Devolver confirmación
        return AssignmentResponse.builder()
                // Asumo que tu entidad Actividad tiene un método getNombre()
                .mensaje("Actividad '" + actividad.getTitulo() + "' asignada a la clase " + clase.getNombreClase()
                        + ". Límite: " + request.getFechaLimite() + ".")
                .idAsignacion(asignacionGuardada.getIdAsignacion())
                .build();
    }
}