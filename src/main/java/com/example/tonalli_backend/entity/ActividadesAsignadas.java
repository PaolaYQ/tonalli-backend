package com.example.tonalli_backend.entity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
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
public class ActividadesAsignadas {
    @Id
    @Column(name = "id_asignacion")
    private Integer idAsignacion;

    @Column(name = "id_actividad")
    private Integer idActividad;
    @Column(name = "id_clase")
    private Integer idClase;
    @Column(name = "fecha_asignacion")
    private String fechaAsignacion;
    @Column(name = "fecha_limite")
    private String fechaLimite;
    @Column(name = "completada")
    private Boolean completada;

    @OneToOne
    @JoinColumn(name = "id_clases")
    private Clases clases;
    
    @OneToMany
    @JoinColumn(name = "id_actividad")
    private IntentosActividad intentosActividad;
  
}
