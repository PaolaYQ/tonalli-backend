package com.example.tonalli_backend.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.tonalli_backend.dto.ClassResponse;
import com.example.tonalli_backend.dto.TeacherProfileResponse;
import com.example.tonalli_backend.service.TeacherService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/v1/user")
@RequiredArgsConstructor
public class UserController {

    private final TeacherService teacherService;

    @GetMapping("/profile")
    public ResponseEntity<TeacherProfileResponse> getUserProfile(Authentication authentication) {
        String correo = authentication.getName();
        return ResponseEntity.ok(teacherService.getProfile(correo));
    }

    @GetMapping("/classes")
    public ResponseEntity<List<ClassResponse>> getTeacherClasses(Authentication authentication) {
        String correo = authentication.getName();
        // Llamamos al método que ya creaste en TeacherService
        return ResponseEntity.ok(teacherService.getTeacherClasses(correo));
    }

}
