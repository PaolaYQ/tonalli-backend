package com.example.tonalli_backend.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "alumno_item")
public class AlumnoItem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_alumno_item")
    private Integer id;

    @CreationTimestamp
    @Column(name = "adquirido_en", updatable = false)
    private LocalDateTime adquiridoEn;

    @Column(name = "en_uso", columnDefinition = "boolean default false")
    private Boolean enUso;

    // --- Relaciones Propietarias (Owning Side) N-a-1 ---
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_alumno", nullable = false)
    private Alumno alumno;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_item", nullable = false)
    private TiendaCosmetico item;
}