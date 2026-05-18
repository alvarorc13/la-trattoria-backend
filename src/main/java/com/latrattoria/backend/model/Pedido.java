package com.latrattoria.backend.model;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "Pedido")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Pedido {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_pedido")
    private Integer id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_mesa", nullable = false)
    private Mesa mesa;

    @Column(name = "fecha_hora", nullable = false)
    private LocalDateTime fechaHora;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Modalidad modalidad = Modalidad.mesa;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Estado estado = Estado.nuevo;

    @Enumerated(EnumType.STRING)
    @Column(name = "metodo_pago", nullable = false)
    private MetodoPago metodoPago;

    @OneToMany(mappedBy = "pedido", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<DetallePedido> detalles;

    public enum Modalidad {
        mesa, recogida
    }
    public enum Estado {
        nuevo, en_preparacion, listo, entregado, cancelado
    }
    public enum MetodoPago {
        tarjeta, PayPal, Bizum, efectivo
    }
    @Override
    public String toString() {
        return "Pedido{" +
                "id=" + id +
                ", mesa=" + (mesa != null ? mesa.getId() : null) +
                ", fechaHora=" + fechaHora +
                ", modalidad=" + modalidad +
                ", estado=" + estado +
                ", metodoPago=" + metodoPago +
                ", detalles.size=" + (detalles != null ? detalles.size() : 0) +
                '}';
    }
}
