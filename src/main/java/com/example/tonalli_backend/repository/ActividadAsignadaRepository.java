package com.example.tonalli_backend.repository;

import com.example.tonalli_backend.entity.ActividadAsignada;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface ActividadAsignadaRepository extends JpaRepository<ActividadAsignada, Integer> {

    // Buscar asignaciones por el código de la clase
    List<ActividadAsignada> findByClase_CodigoClase(String codigoClase);
}