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
@Table(name = "actividades_asignadas")
public class ActividadAsignada {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_asignacion")
    private Integer idAsignacion;

    @CreationTimestamp
    @Column(name = "fecha_asignacion", updatable = false)
    private LocalDateTime fechaAsignacion;

    @Column(name = "fecha_limite", nullable = false)
    private LocalDateTime fechaLimite;

    @Column(name = "numero_intentos", columnDefinition = "integer default 1")
    private Integer numeroIntentos;

    @Column(name = "retroalimentacion_inmediata", columnDefinition = "boolean default true")
    private Boolean retroalimentacionInmediata;

    @Column(name = "mostrar_solucion", columnDefinition = "boolean default false")
    private Boolean mostrarSolucion;

    @Column(columnDefinition = "boolean default false")
    private Boolean completada;

    // --- Relaciones Propietarias (Owning Side) N-a-1 ---
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_actividad", nullable = false)
    private Actividad actividad;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_clase", nullable = false)
    private Clase clase;

    // --- Relación Inversa (Inverse Side) 1-a-N ---
    @OneToMany(mappedBy = "actividadAsignada", cascade = CascadeType.ALL, orphanRemoval = true)
    @Builder.Default
    private Set<IntentoActividad> intentos = new HashSet<>();
}