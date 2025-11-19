package com.example.tonalli_backend.repository;

import java.util.List;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.example.tonalli_backend.entity.Alumno;

public interface AlumnoRepository extends JpaRepository<Alumno, Integer> {
    // Consulta para obtener alumnos de una clase por código, ordenados por
    // estrellas
    @Query("SELECT a FROM Alumno a " +
            "JOIN a.alumnoClases ac " +
            "WHERE ac.clase.codigoClase = :codigoClase " +
            "ORDER BY a.estrellasTotales DESC")
    List<Alumno> findTopStudentsByClassCode(@Param("codigoClase") String codigoClase, Pageable pageable);
}