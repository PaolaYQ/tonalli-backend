package com.example.tonalli_backend.service;

import java.security.SecureRandom;
import java.util.Base64;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.data.domain.PageRequest;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.example.tonalli_backend.dto.ClassActivityResponse;
import com.example.tonalli_backend.dto.ClassDetailResponse;
import com.example.tonalli_backend.dto.ClassResponse;
import com.example.tonalli_backend.dto.CreateClassRequest;
import com.example.tonalli_backend.dto.StudentRankResponse;
import com.example.tonalli_backend.entity.Actividad;
import com.example.tonalli_backend.entity.ActividadAsignada;
import com.example.tonalli_backend.entity.Alumno;
import com.example.tonalli_backend.entity.Clase;
import com.example.tonalli_backend.entity.Maestro;
import com.example.tonalli_backend.entity.Usuario;
import com.example.tonalli_backend.repository.ActividadAsignadaRepository;
import com.example.tonalli_backend.repository.ActividadRepository;
import com.example.tonalli_backend.repository.AlumnoRepository;
import com.example.tonalli_backend.repository.ClaseRepository;
import com.example.tonalli_backend.repository.MaestroRepository;
import com.example.tonalli_backend.repository.UserRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ClassService {

    private final ClaseRepository claseRepository;
    private final UserRepository userRepository;
    private final MaestroRepository maestroRepository;
    private final AlumnoRepository alumnoRepository;
    private final ActividadRepository actividadRepository;
    private final ActividadAsignadaRepository actividadAsignadaRepository;

    // Caracteres permitidos para el código (evitamos O, 0, I, 1 para no confundir)
    private static final String CHARACTERS = "ABCDEFGHJKLMNPQRSTUVWXYZ23456789";
    private static final int CODE_LENGTH = 6;
    private final SecureRandom random = new SecureRandom();

    // CAMBIO 1: El método ahora devuelve 'ClassResponse'
    public ClassResponse createClass(String teacherEmail, CreateClassRequest request) {

        if (request.getGrado() < 1 || request.getGrado() > 6) {
            throw new IllegalArgumentException("El grado debe estar entre 1 y 6.");
        }

        Usuario user = userRepository.findByCorreo(teacherEmail)
                .orElseThrow(() -> new UsernameNotFoundException("Usuario no encontrado"));

        Maestro maestro = maestroRepository.findById(user.getIdUsuario())
                .orElseThrow(() -> new RuntimeException("El usuario no es un maestro registrado"));

        String uniqueCode = generateUniqueCode();

        Clase nuevaClase = Clase.builder()
                .nombreClase(request.getNombreClase())
                .grado(request.getGrado())
                .codigoClase(uniqueCode)
                .maestro(maestro)
                .build();

        // Guardamos y capturamos la entidad guardada (que ya tiene ID)
        Clase claseGuardada = claseRepository.save(nuevaClase);

        // CAMBIO 2: Construimos y retornamos el ClassResponse completo
        return ClassResponse.builder()
                .idClase(claseGuardada.getIdClase())
                .nombreClase(claseGuardada.getNombreClase())
                .grado(claseGuardada.getGrado())
                .codigoClase(claseGuardada.getCodigoClase())
                .totalAlumnos(0) // Al crearla, siempre empieza con 0 alumnos
                .build();
    }

    // --- MÉTODO 1: Obtener Top 3 Estudiantes ---
    public List<StudentRankResponse> getTopStudents(String classCode) {
        // Usamos PageRequest para limitar la consulta a 3 resultados
        List<Alumno> topAlumnos = alumnoRepository.findTopStudentsByClassCode(classCode, PageRequest.of(0, 3));

        return topAlumnos.stream().map(alumno -> {
            String avatarDataUri = null;
            if (alumno.getUsuario().getAvatarBits() != null) {
                String base64Image = Base64.getEncoder().encodeToString(alumno.getUsuario().getAvatarBits());
                avatarDataUri = "data:image/jpeg;base64," + base64Image;
            }

            return StudentRankResponse.builder()
                    .idAlumno(alumno.getIdAlumno())
                    .nombre(alumno.getUsuario().getNombre())
                    .avatarUrl(avatarDataUri)
                    .estrellas(alumno.getEstrellasTotales())
                    .build();
        }).collect(Collectors.toList());
    }

    // --- MÉTODO 2: Obtener Actividades con Estado ---
    public List<ClassActivityResponse> getClassActivitiesStatus(String classCode) {

        // 1. Obtener TODAS las actividades disponibles en el sistema
        List<Actividad> todasLasActividades = actividadRepository.findAll();

        // 2. Obtener las actividades que YA están asignadas a esta clase
        List<ActividadAsignada> asignadas = actividadAsignadaRepository.findByClase_CodigoClase(classCode);

        // 3. Crear un Set de IDs asignados para búsqueda rápida (O(1))
        Set<Integer> idsAsignados = asignadas.stream()
                .map(a -> a.getActividad().getIdActividad())
                .collect(Collectors.toSet());

        // 4. Mapear y marcar el booleano 'asignada'
        return todasLasActividades.stream().map(actividad -> {
            boolean isAssigned = idsAsignados.contains(actividad.getIdActividad());

            return ClassActivityResponse.builder()
                    .idActividad(actividad.getIdActividad())
                    .titulo(actividad.getTitulo())
                    .descripcion(actividad.getDescripcion())
                    .tema(actividad.getTopic().getNombreTopic()) // Asumiendo relación con Topic
                    .asignada(isAssigned) // <-- Aquí está la magia
                    .build();
        }).collect(Collectors.toList());
    }

    // --- Métodos Auxiliares ---

    private String generateUniqueCode() {
        String code;
        do {
            code = generateRandomString();
        } while (claseRepository.existsByCodigoClase(code)); // Verifica que no exista en la BD
        return code;
    }

    private String generateRandomString() {
        StringBuilder sb = new StringBuilder(CODE_LENGTH);
        for (int i = 0; i < CODE_LENGTH; i++) {
            sb.append(CHARACTERS.charAt(random.nextInt(CHARACTERS.length())));
        }
        return sb.toString();
    }

    // --- MÉTODO 3: Obtener Detalles de la Clase por Código ---
    public ClassDetailResponse getClassDetails(String classCode) {

        Clase clase = claseRepository.findByCodigoClase(classCode)
                .orElseThrow(() -> new RuntimeException("No se encontró ninguna clase con el código: " + classCode));

        return ClassDetailResponse.builder()
                .nombreClase(clase.getNombreClase())
                .codigoClase(clase.getCodigoClase())
                .grado(clase.getGrado())
                .totalAlumnos(clase.getAlumnoClases().size())
                .build();
    }
}