package com.example.tonalli_backend.service;

import com.example.tonalli_backend.dto.EnrollmentRequest;
import com.example.tonalli_backend.dto.EnrollmentResponse;
import com.example.tonalli_backend.entity.Alumno;
import com.example.tonalli_backend.entity.AlumnoClase;
import com.example.tonalli_backend.entity.Clase;
import com.example.tonalli_backend.entity.Usuario;
import com.example.tonalli_backend.repository.AlumnoClaseRepository;
import com.example.tonalli_backend.repository.AlumnoRepository;
import com.example.tonalli_backend.repository.ClaseRepository;
import com.example.tonalli_backend.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class EnrollmentService {

    private final UserRepository userRepository;
    private final AlumnoRepository alumnoRepository;
    private final ClaseRepository claseRepository;
    private final AlumnoClaseRepository alumnoClaseRepository;

    @Transactional
    public EnrollmentResponse enrollStudent(String studentEmail, EnrollmentRequest request) {
        
        // 1. Encontrar la clase por el código
        Clase clase = claseRepository.findByCodigoClase(request.getCodigoClase())
                .orElseThrow(() -> new IllegalArgumentException("Código de clase inválido o no existe."));

        // 2. Encontrar al Alumno (usando el correo del token)
        Usuario user = userRepository.findByCorreo(studentEmail)
                .orElseThrow(() -> new UsernameNotFoundException("Usuario no encontrado"));
        
        Alumno alumno = alumnoRepository.findById(user.getIdUsuario())
                .orElseThrow(() -> new RuntimeException("El usuario no tiene perfil de alumno."));

        // 3. Verificar si ya está inscrito
        if (alumnoClaseRepository.existsByAlumno_IdAlumnoAndClase_IdClase(
                alumno.getIdAlumno(), clase.getIdClase())) {
            
            throw new IllegalStateException("El alumno ya se encuentra inscrito en esta clase.");
        }

        // 4. Crear la relación (la inscripción)
        AlumnoClase inscripcion = AlumnoClase.builder()
                .alumno(alumno)
                .clase(clase)
                .build();
        
        AlumnoClase inscripcionGuardada = alumnoClaseRepository.save(inscripcion);

        // 5. Devolver confirmación
        return EnrollmentResponse.builder()
                .mensaje("Inscripción exitosa a la clase " + clase.getNombreClase() + ".")
                .idAlumnoClase(inscripcionGuardada.getId())
                .build();
    }
}