package com.example.tonalli_backend.repository;

import com.example.tonalli_backend.entity.AlumnoClase;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AlumnoClaseRepository extends JpaRepository<AlumnoClase, Integer> {

    // Método para verificar si el par (Alumno, Clase) ya existe
    boolean existsByAlumno_IdAlumnoAndClase_IdClase(Integer alumnoId, Integer claseId);
}