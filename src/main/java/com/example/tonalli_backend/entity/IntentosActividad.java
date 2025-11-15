package com.example.tonalli_backend.entity;

import javax.swing.text.StyleContext.SmallAttributeSet;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
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
public class IntentosActividad {
    @Id
    @Column(name = "id_intento")
    private Integer idIntento;

    @Column(name = "id_alumno")
    private Integer idAlumno;

    @Column(name = "fecha_intento")
    private Integer fechaIntento;

    @Column(name = "resultado_porcentaje")
    private Integer resultadoPorcentaje;
    @Column(name = "estrellas_otorgadas")
    private Integer estrellasOtorgadas;
    @Column(name = "duracion_segundos")
    private Integer duracionSegundos;

    @OneToMany
    @JoinColumn(name = "id_asignacion")
    private ActividadesAsignadas actividadesAsignadas;

    @ManyToOne
    @JoinColumn(name = "id_alumno")
    private Alumno alumno;
}

