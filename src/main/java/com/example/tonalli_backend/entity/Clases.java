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
public class Clases {
    @Id
    @Column(name = "id_clases")
    private Integer idClases;

    @Column(name = "id_maestra")
    private Integer idMaestra;
    @Column(name = "nomvbre_clase")
    private String nombreClase;
    @Column(name = "grado")
    private Integer grado;
    @Column(name = "Codigo_clase")
    private String codigoClase;

    @ManyToOne
    @JoinColumn(name = "id_maestra")
    private Maestro maestro;

}
