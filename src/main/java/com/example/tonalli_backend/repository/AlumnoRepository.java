package com.example.tonalli_backend.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.tonalli_backend.entity.Alumno;

public interface AlumnoRepository extends JpaRepository<Alumno, Integer> { 
    
}