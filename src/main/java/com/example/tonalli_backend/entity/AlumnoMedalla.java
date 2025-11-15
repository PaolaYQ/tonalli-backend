package com.example.tonalli_backend.entity;
import javax.swing.text.StyleContext.SmallAttributeSet;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
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
public class AlumnoMedalla {
    @Id
    @Column(name = "id_alumno_medalla")
    private Integer idAlumnoMedalla;

    @Column(name = "id_alumno")
    private Integer idAlumno;
    @Column(name = "id_medalla")
    private Integer idMedalla;
    @Column(name = "fecha_obtenida")
    private Integer fechaObtenida;

    @ManyToOne
    @JoinColumn(name = "id_alumno")
    private Alumno alumno;

    @ManyToOne
    @JoinColumn(name = "id_medalla")
    private Medalla medalla;
}
