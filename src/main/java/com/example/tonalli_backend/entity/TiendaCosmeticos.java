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
public class TiendaCosmeticos {
    @Id
    @Column(name = "id_item")
    private Integer idItem;

    @Column(name = "id_tipo")
    private Integer idTipo;
    @Column(name = "nombre")
    private String nombre;
    @Column(name = "precio_estrellas")
    private Integer precioEstrellas;
    @Column(name = "icono_url")
    private String iconoUrl;

    @OneToOne
    @JoinColumn(name = "id_tipo")
    private TiposCosmeticos tiposCosmeticos;
    
}
