package com.example.tonalli_backend.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "intentos_actividad")
public class IntentoActividad {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_intento")
    private Integer idIntento;

    @CreationTimestamp
    @Column(name = "fecha_intento", updatable = false)
    private LocalDateTime fechaIntento;

    @Column(name = "resultado_porcentaje", nullable = false)
    private BigDecimal resultadoPorcentaje;

    @Column(name = "estrellas_otorgadas")
    private Integer estrellasOtorgadas;

    @Column(name = "duracion_segundos")
    private Integer duracionSegundos;

    // --- Relaciones Propietarias (Owning Side) N-a-1 ---
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_asignacion", nullable = false)
    private ActividadAsignada actividadAsignada;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_alumno", nullable = false)
    private Alumno alumno;
}