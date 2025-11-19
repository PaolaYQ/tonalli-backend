package com.example.tonalli_backend.entity;

import java.util.HashSet;
import java.util.Set;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Builder
@Getter 
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "clases")
@Entity
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

    // --- Relación Propietaria (Owning Side) N-a-1 ---
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_maestra", nullable = false)
    private Maestro maestro;

    // --- Relaciones Inversas (Inverse Side) 1-a-N ---
    @OneToMany(mappedBy = "clase", cascade = CascadeType.ALL, orphanRemoval = true)
    @Builder.Default
    private Set<AlumnoClase> alumnoClases = new HashSet<>();

    @OneToMany(mappedBy = "clase", cascade = CascadeType.ALL, orphanRemoval = true)
    @Builder.Default
    private Set<ActividadAsignada> actividadesAsignadas = new HashSet<>();
}