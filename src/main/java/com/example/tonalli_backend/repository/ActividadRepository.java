package com.example.tonalli_backend.repository;

import com.example.tonalli_backend.entity.Actividad;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ActividadRepository extends JpaRepository<Actividad, Integer> {
    // Usaremos findAll() por defecto
}