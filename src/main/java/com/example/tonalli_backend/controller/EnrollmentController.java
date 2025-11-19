package com.example.tonalli_backend.controller;

import com.example.tonalli_backend.dto.EnrollmentRequest;
import com.example.tonalli_backend.dto.EnrollmentResponse;
import com.example.tonalli_backend.service.EnrollmentService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/enrollment") // Ruta específica para acciones de inscripción
@RequiredArgsConstructor
public class EnrollmentController {

    private final EnrollmentService enrollmentService;

    // POST /api/v1/enrollment
    @PostMapping
    public ResponseEntity<EnrollmentResponse> enrollStudent(
            @RequestBody EnrollmentRequest request,
            Authentication authentication) {
        // Obtenemos el correo del alumno desde el token JWT
        String studentEmail = authentication.getName();

        EnrollmentResponse response = enrollmentService.enrollStudent(studentEmail, request);

        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }
}