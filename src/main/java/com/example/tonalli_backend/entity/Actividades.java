package com.example.tonalli_backend.entity;
import javax.swing.text.StyleContext.SmallAttributeSet;

import org.springframework.security.access.method.P;

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
public class Actividades {
    @Id
    @Column(name = "id_actividad")
    private Integer idActividad;

    @Column(name = "id_medalla")
    private Integer idMedalla;
    @Column(name = "id_topic")
    private Integer idTopic;

    private String titulo;

    private String descripcion; 

    private String tipo;
    @Column(name = "nivel_dificultad")
    private String nivelDificultad;
    @Column(name = "fecha_creacion")
    private String fechaCreacion;
    
    @OneToMany
    @JoinColumn(name = "id_actividad")
    private Preguntas preguntas;
    @OneToOne
    @JoinColumn(name = "id_actividad")
    private ActividadesAsignadas actividadesAsignadas;
    @ManyToOne
    @JoinColumn(name = "id_medalla")
    private Medalla medalla;
    @ManyToOne
    @JoinColumn(name = "id_topic")
    private Topics topics;

    
}
