package com.example.tonalli_backend.controller;

import com.example.tonalli_backend.dto.ClassActivityResponse;
import com.example.tonalli_backend.dto.ClassDetailResponse;
import com.example.tonalli_backend.dto.ClassResponse;
import com.example.tonalli_backend.dto.CreateClassRequest;
import com.example.tonalli_backend.dto.CreateClassResponse;
import com.example.tonalli_backend.dto.StudentRankResponse;
import com.example.tonalli_backend.service.ClassService;
import lombok.RequiredArgsConstructor;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
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
    // CAMBIO: Ahora devuelve ResponseEntity<ClassResponse>
    public ResponseEntity<ClassResponse> createClass(
            @RequestBody CreateClassRequest request,
            Authentication authentication) {
        String teacherEmail = authentication.getName();

        ClassResponse response = classService.createClass(teacherEmail, request);

        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @GetMapping("/{code}/top-students")
    public ResponseEntity<List<StudentRankResponse>> getTopStudents(@PathVariable String code) {
        return ResponseEntity.ok(classService.getTopStudents(code));
    }

    @GetMapping("/{code}/activities")
    public ResponseEntity<List<ClassActivityResponse>> getClassActivities(@PathVariable String code) {
        return ResponseEntity.ok(classService.getClassActivitiesStatus(code));
    }

    // Endpoint para obtener los detalles generales de la clase
    // GET http://localhost:8081/api/v1/classes/{code}
    @GetMapping("/{code}")
    public ResponseEntity<ClassDetailResponse> getClassDetails(@PathVariable String code) {
        return ResponseEntity.ok(classService.getClassDetails(code));
    }
}