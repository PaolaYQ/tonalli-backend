package com.example.tonalli_backend.entity;

import jakarta.persistence.*;
import lombok.*;
import java.util.Set;
import java.util.HashSet;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "tienda_cosmeticos")
public class TiendaCosmetico {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_item")
    private Integer idItem;

    @Column(nullable = false)
    private String nombre;

    @Column(name = "precio_estrellas", nullable = false)
    private Integer precioEstrellas;

    @Column(name = "icono_url")
    private String iconoUrl;

    // --- Relación Propietaria (Owning Side) N-a-1 ---
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_tipo", nullable = false)
    private TipoCosmetico tipoCosmetico;

    // --- Relación Inversa (Inverse Side) 1-a-N ---
    @OneToMany(mappedBy = "item", cascade = CascadeType.ALL, orphanRemoval = true)
    @Builder.Default
    private Set<AlumnoItem> alumnoItems = new HashSet<>();
}
