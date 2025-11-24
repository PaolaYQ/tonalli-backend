package com.example.tonalli_backend.controller;

import com.example.tonalli_backend.dto.ActivitiesDashboardResponse;
import com.example.tonalli_backend.dto.QuizDataResponse;
import com.example.tonalli_backend.dto.SubmitAttemptRequest;
import com.example.tonalli_backend.dto.SubmitAttemptResponse;
import com.example.tonalli_backend.service.StudentService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/student")
@RequiredArgsConstructor
public class StudentController {

    private final StudentService studentService;

    // Endpoint Simplificado: GET /api/v1/student/my-activities
    @GetMapping("/my-activities")
    public ResponseEntity<ActivitiesDashboardResponse> getMyActivities(
            Authentication authentication) {
        String correo = authentication.getName();
        // El servicio se encarga de buscar la clase del alumno
        return ResponseEntity.ok(studentService.getStudentActivities(correo));
    }

    // Endpoint: GET /api/v1/student/assignment/{id}/quiz
    @GetMapping("/assignment/{id}/quiz")
    public ResponseEntity<QuizDataResponse> getQuizData(
            @PathVariable("id") Integer idAsignacion,
            Authentication authentication) {
        String correo = authentication.getName();
        return ResponseEntity.ok(studentService.getQuizData(correo, idAsignacion));
    }

    // Endpoint: POST /api/v1/student/attempt
    @PostMapping("/attempt")
    public ResponseEntity<SubmitAttemptResponse> submitAttempt(
            @RequestBody SubmitAttemptRequest request,
            Authentication authentication) {
        String correo = authentication.getName();
        return ResponseEntity.ok(studentService.submitAttempt(correo, request));
    }
}