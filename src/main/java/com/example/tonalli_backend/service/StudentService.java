package com.example.tonalli_backend.service;

import com.example.tonalli_backend.dto.ActivitiesDashboardResponse;
import com.example.tonalli_backend.dto.ActivityItem;
import com.example.tonalli_backend.dto.MedalResponse;
import com.example.tonalli_backend.dto.QuestionDTO;
import com.example.tonalli_backend.dto.QuizDataResponse;
import com.example.tonalli_backend.dto.StudentProfileDetailResponse;
import com.example.tonalli_backend.dto.SubmitAttemptRequest;
import com.example.tonalli_backend.dto.SubmitAttemptResponse;
import com.example.tonalli_backend.entity.Actividad;
import com.example.tonalli_backend.entity.ActividadAsignada;
import com.example.tonalli_backend.entity.Alumno;
import com.example.tonalli_backend.entity.AlumnoClase;
import com.example.tonalli_backend.entity.AlumnoMedalla;
import com.example.tonalli_backend.entity.IntentoActividad;
import com.example.tonalli_backend.entity.Medalla;
import com.example.tonalli_backend.entity.Usuario;
import com.example.tonalli_backend.repository.ActividadAsignadaRepository;
import com.example.tonalli_backend.repository.AlumnoMedallaRepository;
import com.example.tonalli_backend.repository.AlumnoRepository;
import com.example.tonalli_backend.repository.IntentoActividadRepository;
import com.example.tonalli_backend.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Base64;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class StudentService {

    private final UserRepository userRepository;
    private final ActividadAsignadaRepository actividadAsignadaRepository;
    private final IntentoActividadRepository intentoRepository;
    private final AlumnoMedallaRepository alumnoMedallaRepository;
    private final AlumnoRepository alumnoRepository;

    @Transactional(readOnly = true) // Importante para leer la lista Lazy de clases
    public ActivitiesDashboardResponse getStudentActivities(String email) {

        // 1. Obtener al Alumno desde el token
        Usuario user = userRepository.findByCorreo(email)
                .orElseThrow(() -> new UsernameNotFoundException("Usuario no encontrado"));

        Alumno alumno = user.getAlumno();
        if (alumno == null) {
            throw new RuntimeException("El usuario no tiene perfil de alumno.");
        }

        // 2. Buscar automáticamente la clase del alumno
        if (alumno.getAlumnoClases().isEmpty()) {
            // Si no tiene clase, devolvemos listas vacías (o lanzamos error, según
            // prefieras)
            return ActivitiesDashboardResponse.builder()
                    .pendientes(List.of())
                    .vencidas(List.of())
                    .completadas(List.of())
                    .build();
        }

        // Tomamos la primera clase (Asumiendo regla de 1 Alumno = 1 Clase)
        AlumnoClase inscripcion = alumno.getAlumnoClases().iterator().next();
        String classCode = inscripcion.getClase().getCodigoClase();

        // 3. Obtener asignaciones de esa clase
        List<ActividadAsignada> todasLasAsignaciones = actividadAsignadaRepository.findByClase_CodigoClase(classCode);

        // 4. Obtener intentos previos del alumno
        List<IntentoActividad> misIntentos = intentoRepository.findByAlumno_IdAlumno(alumno.getIdAlumno());

        // 5. Crear mapa de intentos para búsqueda rápida
        Map<Integer, IntentoActividad> intentosMap = misIntentos.stream()
                .collect(Collectors.toMap(
                        i -> i.getActividadAsignada().getIdAsignacion(),
                        i -> i,
                        (existente, reemplazo) -> existente // Si hay duplicados, conservar uno
                ));

        // 6. Clasificar
        List<ActivityItem> pendientes = new ArrayList<>();
        List<ActivityItem> vencidas = new ArrayList<>();
        List<ActivityItem> completadas = new ArrayList<>();

        LocalDateTime ahora = LocalDateTime.now();

        for (ActividadAsignada asignacion : todasLasAsignaciones) {

            ActivityItem item = ActivityItem.builder()
                    .idAsignacion(asignacion.getIdAsignacion())
                    .idActividad(asignacion.getActividad().getIdActividad())
                    .titulo(asignacion.getActividad().getTitulo())
                    .descripcion(asignacion.getActividad().getDescripcion())
                    .tema(asignacion.getActividad().getTopic().getNombreTopic())
                    .fechaLimite(asignacion.getFechaLimite())
                    .build();

            if (intentosMap.containsKey(asignacion.getIdAsignacion())) {
                completadas.add(item);
            } else if (asignacion.getFechaLimite().isBefore(ahora)) {
                vencidas.add(item);
            } else {
                pendientes.add(item);
            }
        }

        return ActivitiesDashboardResponse.builder()
                .pendientes(pendientes)
                .vencidas(vencidas)
                .completadas(completadas)
                .build();
    }

    @Transactional(readOnly = true)
    public QuizDataResponse getQuizData(String email, Integer idAsignacion) {

        // 1. Obtener al Alumno
        Usuario user = userRepository.findByCorreo(email)
                .orElseThrow(() -> new UsernameNotFoundException("Usuario no encontrado"));
        Alumno alumno = user.getAlumno();

        // 2. Obtener la Asignación
        ActividadAsignada asignacion = actividadAsignadaRepository.findById(idAsignacion)
                .orElseThrow(() -> new IllegalArgumentException("Asignación no encontrada"));

        // 3. VALIDACIÓN DE SEGURIDAD:
        // Verificar que el alumno pertenezca a la clase de esta asignación.
        // Buscamos si en sus inscripciones existe la clase de la asignación.
        boolean inscrito = alumno.getAlumnoClases().stream()
                .anyMatch(ac -> ac.getClase().getIdClase().equals(asignacion.getClase().getIdClase()));

        if (!inscrito) {
            throw new SecurityException("No tienes permiso para ver esta actividad (No perteneces a la clase).");
        }

        // 4. Validar Fechas (Opcional: impedir abrir si ya venció)
        if (asignacion.getFechaLimite().isBefore(LocalDateTime.now())) {
            throw new IllegalStateException("Esta actividad ya ha vencido.");
        }

        // 5. Mapear los datos (Ocultando la respuesta correcta)
        Actividad actividad = asignacion.getActividad();

        List<QuestionDTO> preguntasDTO = actividad.getPreguntas().stream()
                .map(p -> QuestionDTO.builder()
                        .idPregunta(p.getIdPregunta())
                        .enunciado(p.getEnunciado())
                        .opcionA(p.getOpcionA())
                        .opcionB(p.getOpcionB())
                        .opcionC(p.getOpcionC())
                        .opcionD(p.getOpcionD())
                        // AQUÍ ESTÁ EL CAMBIO:
                        .respuestaCorrecta(p.getRespuestaCorrecta())
                        .build())
                .collect(Collectors.toList());

        // --- CORRECCIÓN AQUÍ ---
        return QuizDataResponse.builder()
                // Agrega estas líneas que faltan:
                .idAsignacion(asignacion.getIdAsignacion())
                .tituloActividad(actividad.getTitulo())
                .descripcion(actividad.getDescripcion())
                .fechaLimite(asignacion.getFechaLimite())
                // ---------------------------------------
                .preguntas(preguntasDTO)
                .build();
    }

    @Transactional
    public SubmitAttemptResponse submitAttempt(String email, SubmitAttemptRequest request) {

        // 1. Obtener Usuario y Asignación
        Usuario user = userRepository.findByCorreo(email)
                .orElseThrow(() -> new UsernameNotFoundException("Usuario no encontrado"));
        Alumno alumno = user.getAlumno();

        ActividadAsignada asignacion = actividadAsignadaRepository.findById(request.getIdAsignacion())
                .orElseThrow(() -> new IllegalArgumentException("Asignación no encontrada"));

        // 2. Obtener Historial Previo
        List<IntentoActividad> intentosPrevios = intentoRepository
                .findByActividadAsignada_IdAsignacionAndAlumno_IdAlumno(asignacion.getIdAsignacion(),
                        alumno.getIdAlumno());

        // --- CORRECCIÓN: Manejo de Nulos ---
        if (intentosPrevios == null) {
            intentosPrevios = new ArrayList<>(); // Inicializar vacía si es null
        }
        // -----------------------------------

        // --- CORRECCIÓN 2: Manejar valores nulos en la suma ---
        int estrellasAcumuladas = intentosPrevios.stream()
                .map(IntentoActividad::getEstrellasOtorgadas) // Usar map (Stream<Integer>) no mapToInt
                .filter(estrellas -> estrellas != null) // Filtrar nulos
                .mapToInt(Integer::intValue) // Ahora sí a int
                .sum();

        // Si ya tiene 3 estrellas, no puede ganar más
        if (estrellasAcumuladas >= 3) {
            return SubmitAttemptResponse.builder()
                    .estrellasGanadas(0)
                    .estrellasTotalesActividad(3)
                    .medallaGanada(false)
                    .actividadCompletada(true)
                    .mensaje("Actividad ya completada anteriormente.")
                    .build();
        }

        // 3. Calcular Estrellas a Ganar (Reglas RF-12.2)
        int estrellasAGanar = 0;
        BigDecimal score = request.getResultadoPorcentaje();
        boolean esPerfecto = score.compareTo(new BigDecimal("100")) == 0;

        if (esPerfecto) {
            // Intento Perfecto: Completa hasta 3 estrellas
            estrellasAGanar = 3 - estrellasAcumuladas;
        } else {
            // Intento con errores
            if (intentosPrevios.isEmpty()) {
                // 1er intento con error -> 1 estrella
                estrellasAGanar = 1;
            } else if (intentosPrevios.size() == 1) {
                // 2do intento: Verificar si mejoró
                BigDecimal maxScorePrevio = intentosPrevios.stream()
                        .map(IntentoActividad::getResultadoPorcentaje)
                        .max(Comparator.naturalOrder())
                        .orElse(BigDecimal.ZERO);

                if (score.compareTo(maxScorePrevio) > 0) {
                    // Mejoró -> 1 estrella extra (sin pasar de 3)
                    estrellasAGanar = 1;
                }
            }
            // 3er intento o más con errores -> 0 estrellas
        }

        // Ajuste de seguridad para no exceder 3
        if (estrellasAcumuladas + estrellasAGanar > 3) {
            estrellasAGanar = 3 - estrellasAcumuladas;
        }

        // 4. Guardar el Nuevo Intento
        IntentoActividad nuevoIntento = IntentoActividad.builder()
                .actividadAsignada(asignacion)
                .alumno(alumno)
                .resultadoPorcentaje(score)
                .duracionSegundos(request.getDuracionSegundos())
                .estrellasOtorgadas(estrellasAGanar)
                // fechaIntento es automática por @CreationTimestamp
                .build();

        intentoRepository.save(nuevoIntento);

        // 5. Actualizar Estrellas Globales del Alumno
        if (estrellasAGanar > 0) {
            alumno.setEstrellasTotales(alumno.getEstrellasTotales() + estrellasAGanar);
            alumno.setEstrellasDisponibles(alumno.getEstrellasDisponibles() + estrellasAGanar);
            alumnoRepository.save(alumno);
        }

        // 6. Asignar Medalla (RF-12.4)
        boolean medallaGanada = false;
        if (esPerfecto) {
            Medalla medalla = asignacion.getActividad().getMedalla();

            boolean yaTiene = alumnoMedallaRepository.existsByAlumno_IdAlumnoAndMedalla_IdMedalla(
                    alumno.getIdAlumno(), medalla.getIdMedalla());

            if (!yaTiene) {
                AlumnoMedalla nuevaMedalla = AlumnoMedalla.builder()
                        .alumno(alumno)
                        .medalla(medalla)
                        .build();
                alumnoMedallaRepository.save(nuevaMedalla);
                medallaGanada = true;
            }
        }

        // 7. Construir Respuesta
        int totalFinal = estrellasAcumuladas + estrellasAGanar;

        String mensaje = "Intento registrado.";
        if (estrellasAGanar > 0)
            mensaje = "¡Bien hecho! Ganaste " + estrellasAGanar + " estrellas.";
        if (medallaGanada)
            mensaje = "¡Perfecto! Has ganado una medalla nueva.";

        return SubmitAttemptResponse.builder()
                .estrellasGanadas(estrellasAGanar)
                .estrellasTotalesActividad(totalFinal)
                .medallaGanada(medallaGanada)
                .actividadCompletada(totalFinal == 3)
                .mensaje(mensaje)
                .build();
    }

    @Transactional(readOnly = true)
    public StudentProfileDetailResponse getProfileDetail(String email) {

        Usuario user = userRepository.findByCorreo(email)
                .orElseThrow(() -> new UsernameNotFoundException("Usuario no encontrado"));

        Alumno alumno = user.getAlumno();
        if (alumno == null)
            throw new RuntimeException("Perfil de alumno no encontrado");

        // 1. Convertir Avatar (Bits -> Base64)
        String avatarDataUri = null;
        if (user.getAvatarBits() != null && user.getAvatarBits().length > 0) {
            String base64Image = Base64.getEncoder().encodeToString(user.getAvatarBits());
            avatarDataUri = "data:image/jpeg;base64," + base64Image;
        }

        // 2. Mapear Medallas
        List<MedalResponse> misMedallas = alumno.getAlumnoMedallas().stream()
                .map(am -> MedalResponse.builder()
                        .idMedalla(am.getMedalla().getIdMedalla())
                        .nombre(am.getMedalla().getNombre())
                        .descripcion(am.getMedalla().getDescripcion())
                        .iconoUrl(am.getMedalla().getIconoUrl())
                        .tema(am.getMedalla().getTema())
                        .fechaObtenida(am.getFechaObtenida())
                        .build())
                .collect(Collectors.toList());

        return StudentProfileDetailResponse.builder()
                .nombre(user.getNombre())
                .correo(user.getCorreo())
                .avatarUrl(avatarDataUri)
                .estrellasTotales(alumno.getEstrellasTotales())
                .estrellasDisponibles(alumno.getEstrellasDisponibles())
                .medallas(misMedallas)
                .build();
    }
}