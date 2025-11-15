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

public class Topics {
    @Id
    @Column(name = "id_topic")
    private Integer idTopic;

    @Column(name = "nombre_topic")
    private String nombreTopic;

    @OneToMany
    @JoinColumn(name = "id_topic")
    private Actividades actividades;
    
}
