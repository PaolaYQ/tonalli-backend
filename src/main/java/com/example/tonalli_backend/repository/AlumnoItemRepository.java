package com.example.tonalli_backend.repository;

import com.example.tonalli_backend.entity.AlumnoItem;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface AlumnoItemRepository extends JpaRepository<AlumnoItem, Integer> {
    
    // Obtener inventario del alumno
    List<AlumnoItem> findByAlumno_IdAlumno(Integer idAlumno);
    
    // Verificar propiedad
    boolean existsByAlumno_IdAlumnoAndItem_IdItem(Integer idAlumno, Integer idItem);
}