package com.example.tonalli_backend.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "maestra")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Alumno {
    @Id
    @Column(name = "id_alumno")
    private Integer idAlumno;
    @Column(name = "estrellas_totales")
    private Integer estrellasTotales;
    @Column(name = "estrellas_disponibles")
    private Integer estrellasDisponibles;

    @OneToOne
    @JoinColumn(name = "id_usuario")
    private Usuario usuario;

}
