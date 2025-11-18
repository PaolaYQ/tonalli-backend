package com.example.tonalli_backend.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
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
@Table(name = "maestra")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Maestro {

    @Id
    @Column(name = "id_maestra")
    private Integer idMaestra;

    @OneToOne
    @MapsId
    @JoinColumn(name = "id_maestra")
    private Usuario usuario;

}
