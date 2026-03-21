package com.latrattoria.backend.model;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "Categoria")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Categoria {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_categoria")
    private Integer id;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private NombreCategoria nombre;

    public enum NombreCategoria {
        entrante, principal, postre, bebida
    }
}
