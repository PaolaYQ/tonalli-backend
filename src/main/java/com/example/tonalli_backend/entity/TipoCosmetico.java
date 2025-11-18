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
@Table(name = "tipos_cosmeticos")
public class TipoCosmetico {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_tipo")
    private Integer idTipo;

    @Column(name = "nombre_tipo", nullable = false, unique = true)
    private String nombreTipo;

    // --- Relación Inversa (Inverse Side) 1-a-N ---
    @OneToMany(mappedBy = "tipoCosmetico", cascade = CascadeType.ALL, orphanRemoval = true)
    @Builder.Default
    private Set<TiendaCosmetico> items = new HashSet<>();
}