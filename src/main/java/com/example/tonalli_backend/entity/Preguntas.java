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
public class Preguntas {
    @Id
    @Column(name = "id_pregunta")
    private Integer idPregunta;

    @Column(name = "id_actividad")
    private Integer idActividad;
    @Column(name = "enunciado")
    private String enunciado;
    @Column(name = "opcion_a")
    private String opcionA;
    @Column(name = "opcion_b")
    private String opcionB;
    @Column(name = "opcion_c")
    private String opcionC;
    @Column(name = "opcion_d")
    private String opcionD;
    @Column(name = "respuesta_correcta")
    private String respuestaCorrecta;

    @ManyToOne
    @JoinColumn(name = "id_actividad")
    private Actividades actividades;
    
}
