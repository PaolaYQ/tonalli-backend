package com.example.tonalli_backend.entity;

import jakarta.persistence.*;
import lombok.*;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "preguntas")
public class Pregunta {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_pregunta")
    private Integer idPregunta;

    @Column(nullable = false)
    private String enunciado;

    @Column(name = "opcion_a", nullable = false)
    private String opcionA;

    @Column(name = "opcion_b", nullable = false)
    private String opcionB;

    @Column(name = "opcion_c", nullable = false)
    private String opcionC;

    @Column(name = "opcion_d", nullable = false)
    private String opcionD;

    @Column(name = "respuesta_correcta", nullable = false, columnDefinition = "char(1)")
    private String respuestaCorrecta; // Ej. "A", "B", "C", o "D"

    // --- Relación Propietaria (Owning Side) N-a-1 ---
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_actividad", nullable = false)
    private Actividad actividad;
}