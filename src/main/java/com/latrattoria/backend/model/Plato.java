package com.latrattoria.backend.model;

import jakarta.persistence.*;
import lombok.*;
import java.math.BigDecimal;

@Entity
@Table(name = "Plato")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Plato {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_plato")
    private Integer id;

    @Column(nullable = false, length = 100)
    private String nombre;

    @Column(columnDefinition = "text")
    private String descripcion;

    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal precio;

    @Column(length = 255)
    private String imagen;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Disponibilidad disponibilidad = Disponibilidad.activo;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_categoria")
    private Categoria categoria;

    public enum Disponibilidad {
        activo, inactivo
    }

    public String getNombre() { return nombre; }
    public String getDescripcion() { return descripcion; }
    public BigDecimal getPrecio() { return precio; }
    public String getImagen() { return imagen; }
    public Disponibilidad getDisponibilidad() { return disponibilidad; }
    public Categoria getCategoria() { return categoria; }
    public void setNombre(String nombre) { this.nombre = nombre; }
    public void setDescripcion(String descripcion) { this.descripcion = descripcion; }
    public void setPrecio(BigDecimal precio) { this.precio = precio; }
    public void setImagen(String imagen) { this.imagen = imagen; }
    public void setDisponibilidad(Disponibilidad disponibilidad) { this.disponibilidad = disponibilidad; }
    public void setCategoria(Categoria categoria) { this.categoria = categoria; }
}