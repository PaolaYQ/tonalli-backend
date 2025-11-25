package com.example.tonalli_backend.entity;

import java.util.HashSet;
import java.util.Set;

import jakarta.persistence.*;
import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "alumno")
@EqualsAndHashCode(exclude = {
        "usuario",
        "alumnoClases",
        // Si no tienes estos campos en tu clase todavía, bórralos del exclude para que
        // no marque error:
        "intentos",
        "alumnoMedallas",
        "alumnoItems"
})
public class Alumno {

    @Id
    @Column(name = "id_alumno")
    // ¡ELIMINADO @GeneratedValue! El ID viene del Usuario.
    private Integer idAlumno;

    @Column(name = "estrellas_totales")
    private Integer estrellasTotales;

    @Column(name = "estrellas_disponibles")
    private Integer estrellasDisponibles;

    @OneToOne(fetch = FetchType.LAZY)
    @MapsId
    @JoinColumn(name = "id_alumno")
    @ToString.Exclude // <-- Recomendado: Evita ciclos al imprimir logs
    private Usuario usuario;

    @OneToMany(mappedBy = "alumno", cascade = CascadeType.ALL, orphanRemoval = true)
    @Builder.Default
    @ToString.Exclude // <-- Recomendado
    private Set<AlumnoClase> alumnoClases = new HashSet<>();

    @OneToMany(mappedBy = "alumno", cascade = CascadeType.ALL, orphanRemoval = true)
    @Builder.Default
    @ToString.Exclude
    private Set<AlumnoMedalla> alumnoMedallas = new HashSet<>();
  

    // ... Aquí irían tus otros Sets (intentos, medallas, etc.) si los tienes
    // definidos
}