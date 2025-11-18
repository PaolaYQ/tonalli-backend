package com.example.tonalli_backend.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.MapsId;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "alumno")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Alumno {
    @Id
    @Column(name = "id_alumno")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer idAlumno;
    @Column(name = "estrellas_totales")
    private Integer estrellasTotales;
    @Column(name = "estrellas_disponibles")
    private Integer estrellasDisponibles;

    @OneToOne
    @MapsId
    @JoinColumn(name = "id_alumno")
    private Usuario usuario;

}
