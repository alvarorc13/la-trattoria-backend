package com.latrattoria.backend.model;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "UsuarioSistema")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class UsuarioSistema {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_usuario")
    private Integer id;

    @Column(nullable = false, length = 100)
    private String nombre;

    @Column(nullable = false, length = 150, unique = true)
    private String email;

    @Column(nullable = false, length = 255)
    private String passwordHash;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Rol rol;

    @Column(nullable = false)
    private Boolean activo = true;

    public enum Rol {
        personal, administrador
    }

    public String getNombre() { return nombre; }
    public String getEmail() { return email; }
    public String getPasswordHash() { return passwordHash; }
    public UsuarioSistema.Rol getRol() { return rol; }
    public Boolean getActivo() { return activo; }
}