package com.latrattoria.backend.model;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "Mesa")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Mesa {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_mesa")
    private Integer id;

    @Column(nullable = false, unique = true)
    private Integer numero;

    @Column(nullable = false, unique = true, length = 255)
    private String codigoQr;

    @Column(nullable = false)
    private Integer capacidad = 4;

    @Column(nullable = false)
    private Boolean disponible = true;
}
