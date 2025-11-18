package com.example.tonalli_backend.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import java.time.LocalDateTime;
import java.util.Set;
import java.util.HashSet;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "actividades")
public class Actividad {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_actividad")
    private Integer idActividad;

    @Column(nullable = false)
    private String titulo;
    
    private String descripcion;
    private String tipo;
    
    @Column(name = "nivel_dificultad")
    private String nivelDificultad;

    @CreationTimestamp
    @Column(name = "fecha_creacion", updatable = false)
    private LocalDateTime fechaCreacion;

    // --- Relaciones Propietarias (Owning Side) N-a-1 ---
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_medalla", nullable = false)
    private Medalla medalla;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_topic", nullable = false)
    private Topic topic;

    // --- Relaciones Inversas (Inverse Side) 1-a-N ---
    @OneToMany(mappedBy = "actividad", cascade = CascadeType.ALL, orphanRemoval = true)
    @Builder.Default
    private Set<Pregunta> preguntas = new HashSet<>();

    @OneToMany(mappedBy = "actividad", cascade = CascadeType.ALL, orphanRemoval = true)
    @Builder.Default
    private Set<ActividadAsignada> asignaciones = new HashSet<>();
}
