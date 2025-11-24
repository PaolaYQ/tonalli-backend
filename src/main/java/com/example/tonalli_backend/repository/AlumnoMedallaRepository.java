package com.example.tonalli_backend.repository;

import com.example.tonalli_backend.entity.AlumnoMedalla;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AlumnoMedallaRepository extends JpaRepository<AlumnoMedalla, Integer> {
    // Verifica si ya tiene la medalla para no duplicarla
    boolean existsByAlumno_IdAlumnoAndMedalla_IdMedalla(Integer idAlumno, Integer idMedalla);
}