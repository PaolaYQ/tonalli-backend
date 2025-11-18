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
@Table(name = "topics")
public class Topic {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_topic")
    private Integer idTopic;

    @Column(name = "nombre_topic", nullable = false, unique = true)
    private String nombreTopic;

    // --- Relación Inversa (Inverse Side) 1-a-N ---
    @OneToMany(mappedBy = "topic", cascade = CascadeType.ALL, orphanRemoval = true)
    @Builder.Default
    private Set<Actividad> actividades = new HashSet<>();
}