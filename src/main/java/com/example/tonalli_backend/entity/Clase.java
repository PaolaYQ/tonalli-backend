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
@Table(name = "clases")
@EqualsAndHashCode(exclude = { "maestro", "alumnoClases", "actividadesAsignadas" })
public class Clase {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_clase")
    private Integer idClase;

    @Column(name = "nombre_clase", nullable = false)
    private String nombreClase;

    @Column(nullable = false)
    private Short grado;

    @Column(name = "codigo_clase", nullable = false, unique = true)
    private String codigoClase;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_maestra", nullable = false)
    @ToString.Exclude // <-- Importante
    private Maestro maestro;

    @OneToMany(mappedBy = "clase", cascade = CascadeType.ALL, orphanRemoval = true)
    @Builder.Default
    @ToString.Exclude // <-- Importante
    private Set<AlumnoClase> alumnoClases = new HashSet<>();

    @OneToMany(mappedBy = "clase", cascade = CascadeType.ALL, orphanRemoval = true)
    @Builder.Default
    @ToString.Exclude // <-- Importante
    private Set<ActividadAsignada> actividadesAsignadas = new HashSet<>();
}