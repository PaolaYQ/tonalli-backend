package com.example.tonalli_backend.controller;

import com.example.tonalli_backend.dto.ActivityAssignmentRequest;
import com.example.tonalli_backend.dto.AssignmentResponse;
import com.example.tonalli_backend.service.AssignmentService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/v1/assignments") // Nuevo controlador dedicado
@RequiredArgsConstructor
public class AssignmentController {

    private final AssignmentService assignmentService;

    // POST /api/v1/assignments
    @PostMapping
    public ResponseEntity<AssignmentResponse> assignActivityToClass(
            @Valid @RequestBody ActivityAssignmentRequest request) {
        // NOTA: Se debe asegurar que solo un MAESTRO pueda acceder a este endpoint
        // y que este maestro sea el dueño de la clase (la autenticación/autorización
        // se maneja en tu capa de Spring Security).

        AssignmentResponse response = assignmentService.createAssignment(request);

        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }
}