package com.example.tonalli_backend.controller;

import com.example.tonalli_backend.dto.CreateClassRequest;
import com.example.tonalli_backend.dto.CreateClassResponse;
import com.example.tonalli_backend.service.ClassService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/classes") // Ruta base para clases
@RequiredArgsConstructor
public class ClassController {

    private final ClassService classService;

    @PostMapping
    public ResponseEntity<CreateClassResponse> createClass(
            @RequestBody CreateClassRequest request,
            Authentication authentication) {
        // 1. Extraer el email del token
        String teacherEmail = authentication.getName();

        // 2. Llamar al servicio
        CreateClassResponse response = classService.createClass(teacherEmail, request);

        // 3. Retornar 201 Created
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }
}