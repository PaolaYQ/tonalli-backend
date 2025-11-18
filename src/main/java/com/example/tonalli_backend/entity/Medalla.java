package com.example.tonalli_backend.entity;

import jakarta.persistence.*;
import lombok.*;
import java.util.Set;
import java.util.HashSet;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "medallas")
public class Medalla {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_medalla")
    private Integer idMedalla;

    @Column(nullable = false)
    private String nombre;
    
    private String descripcion;
    private String tema;
    
    @Column(name = "icono_url")
    private String iconoUrl;

    // --- Relaciones Inversas (Inverse Side) 1-a-N ---
    @OneToMany(mappedBy = "medalla")
    @Builder.Default
    private Set<Actividad> actividades = new HashSet<>();

    @OneToMany(mappedBy = "medalla", cascade = CascadeType.ALL, orphanRemoval = true)
    @Builder.Default
    private Set<AlumnoMedalla> alumnoMedallas = new HashSet<>();
}