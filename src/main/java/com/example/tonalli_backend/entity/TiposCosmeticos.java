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
public class TiposCosmeticos {
    @Id
    @Column(name = "id_tipo")
    private Integer idTipo;

    @Column(name = "nombre_tipo")
    private String nombreTipo;

    
}
