package com.example.tonalli_backend.entity;

import java.util.HashSet;
import java.util.Set;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.MapsId;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@Table(name = "alumno")
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(exclude = { 
    "usuario",           // Relación 1-a-1
    "alumnoClases",      // Colección
    "intentos",          // Colección
    "alumnoMedallas",    // Colección
    "alumnoItems"        // Colección
})
public class Alumno {
    @Id
    @Column(name = "id_alumno")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer idAlumno;
    @Column(name = "estrellas_totales")
    private Integer estrellasTotales;
    @Column(name = "estrellas_disponibles")
    private Integer estrellasDisponibles;

    @OneToOne
    @MapsId
    @JoinColumn(name = "id_alumno")
    private Usuario usuario;

    // En Alumno.java
    @OneToMany(mappedBy = "alumno", cascade = CascadeType.ALL, orphanRemoval = true)
    @Builder.Default
    private Set<AlumnoClase> alumnoClases = new HashSet<>(); // <-- El nombre debe ser 'alumnoClases'

}
