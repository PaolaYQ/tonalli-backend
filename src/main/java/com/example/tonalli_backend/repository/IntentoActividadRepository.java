package com.example.tonalli_backend.repository;

import com.example.tonalli_backend.entity.IntentoActividad;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface IntentoActividadRepository extends JpaRepository<IntentoActividad, Integer> {

    // Busca todos los intentos que ha hecho un alumno específico
    List<IntentoActividad> findByAlumno_IdAlumno(Integer idAlumno);

    // Recupera el historial de intentos de un alumno para una tarea específica
    List<IntentoActividad> findByActividadAsignada_IdAsignacionAndAlumno_IdAlumno(Integer idAsignacion,
            Integer idAlumno);
}