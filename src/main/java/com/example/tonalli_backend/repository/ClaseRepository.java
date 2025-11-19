package com.example.tonalli_backend.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.tonalli_backend.entity.Clase;

public interface ClaseRepository extends JpaRepository<Clase, Integer> {

    List<Clase> findByMaestro_IdMaestra(Integer idMaestra);

    // Agrega esto dentro de la interfaz
    boolean existsByCodigoClase(String codigoClase);

    // --- AGREGA ESTE NUEVO ---
    // Busca una clase específica por su código único
    Optional<Clase> findByCodigoClase(String codigoClase);

}