package com.latrattoria.backend.model;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "DetallePedido")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class DetallePedido {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_detalle")
    private Integer id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_pedido", nullable = false)
    private Pedido pedido;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_plato", nullable = false)
    private Plato plato;

    @Column(nullable = false)
    private Integer cantidad;

    @Column(name = "instrucciones_especiales", columnDefinition = "text")
    private String instruccionesEspeciales;
    @Override
    public String toString() {
        return "DetallePedido{" +
                "id=" + id +
                ", pedidoId=" + (pedido != null ? pedido.getId() : null) +
                ", platoId=" + (plato != null ? plato.getId() : null) +
                ", cantidad=" + cantidad +
                '}';
    }
}
