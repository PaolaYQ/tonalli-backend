package com.example.tonalli_backend.service;

import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.example.tonalli_backend.dto.ClassResponse;
import com.example.tonalli_backend.dto.TeacherProfileResponse;
import com.example.tonalli_backend.entity.Clase;
import com.example.tonalli_backend.entity.Maestro;
import com.example.tonalli_backend.entity.Usuario;
import com.example.tonalli_backend.repository.ClaseRepository;
import com.example.tonalli_backend.repository.MaestroRepository;
import com.example.tonalli_backend.repository.UserRepository;
import java.util.Base64;
import java.util.List;
import java.util.stream.Collectors;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class TeacherService {

    private final UserRepository userRepository;
    private final MaestroRepository maestroRepository;
    private final ClaseRepository claseRepository;

    public TeacherProfileResponse getProfile(String correo) {

        Usuario user = userRepository.findByCorreo(correo)
                .orElseThrow(() -> new UsernameNotFoundException("Usuario no encontrado"));
        String avatarDataUri = null;

        if (user.getAvatarBits() != null && user.getAvatarBits().length > 0) {
            String base64Image = Base64.getEncoder().encodeToString(user.getAvatarBits());
            avatarDataUri = "data:image/jpeg;base64," + base64Image;
        }

        return TeacherProfileResponse.builder()
                .nombre(user.getNombre())
                .avatarUrl(avatarDataUri)
                .build();
    }

    // --- Obtener Clases del Maestro ---
    public List<ClassResponse> getTeacherClasses(String correo) {
        // 1. Buscar al Usuario Base
        Usuario user = userRepository.findByCorreo(correo)
                .orElseThrow(() -> new UsernameNotFoundException("Usuario no encontrado"));

        // 2. Buscar la entidad Maestro usando el ID del usuario (Relación 1 a 1
        // @MapsId)
        Maestro maestro = maestroRepository.findById(user.getIdUsuario())
                .orElseThrow(() -> new RuntimeException("El usuario no es un maestro registrado"));

        // 3. Buscar las clases de ese maestro
        List<Clase> clases = claseRepository.findByMaestro_IdMaestra(maestro.getIdMaestra());

        // 4. Convertir Entidades a DTOs
        return clases.stream().map(clase -> ClassResponse.builder()
                .idClase(clase.getIdClase())
                .nombreClase(clase.getNombreClase())
                .grado(clase.getGrado())
                .codigoClase(clase.getCodigoClase())
                .totalAlumnos(clase.getAlumnoClases().size()) // Calculamos el total
                .build()).collect(Collectors.toList());
    }

}
