package com.example.tonalli_backend.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.tonalli_backend.entity.Clase;

public interface ClaseRepository extends JpaRepository<Clase, Integer> {

    List<Clase> findByMaestro_IdMaestra(Integer idMaestra);

    // Agrega esto dentro de la interfaz
    boolean existsByCodigoClase(String codigoClase);

}