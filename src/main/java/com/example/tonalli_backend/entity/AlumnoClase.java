package com.example.tonalli_backend.entity;

import jakarta.persistence.*;
import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "alumno_clase")
@EqualsAndHashCode(exclude = { "alumno", "clase" })
public class AlumnoClase {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_alumno_clase")
    private Integer id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_alumno", nullable = false)
    @ToString.Exclude // <-- Importante: Rompe el ciclo al imprimir logs
    private Alumno alumno;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_clase", nullable = false)
    @ToString.Exclude // <-- Importante
    private Clase clase;
}