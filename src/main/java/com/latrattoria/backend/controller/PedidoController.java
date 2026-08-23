package com.latrattoria.backend.controller;

import com.latrattoria.backend.model.DetallePedido;
import com.latrattoria.backend.model.Mesa;
import com.latrattoria.backend.model.Pedido;
import com.latrattoria.backend.model.Plato;
import com.latrattoria.backend.repository.MesaRepository;
import com.latrattoria.backend.repository.PedidoRepository;
import com.latrattoria.backend.repository.PlatoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/v1/pedidos")
public class PedidoController {

    @Autowired
    private PedidoRepository pedidoRepository;
    @Autowired
    private MesaRepository mesaRepository;
    @Autowired
    private PlatoRepository platoRepository;

    // DTO para la request de crear pedido
    public static class LineaRequest {
        private Integer platoId;
        private Integer cantidad;

        public Integer getPlatoId() { return platoId; }
        public void setPlatoId(Integer platoId) { this.platoId = platoId; }
        public Integer getCantidad() { return cantidad; }
        public void setCantidad(Integer cantidad) { this.cantidad = cantidad; }
    }

    public static class CrearPedidoRequest {
        private Integer mesaId;
        private String metodoPago;
        private List<LineaRequest> lineas;

        public Integer getMesaId() { return mesaId; }
        public void setMesaId(Integer mesaId) { this.mesaId = mesaId; }
        public String getMetodoPago() { return metodoPago; }
        public void setMetodoPago(String metodoPago) { this.metodoPago = metodoPago; }
        public List<LineaRequest> getLineas() { return lineas; }
        public void setLineas(List<LineaRequest> lineas) { this.lineas = lineas; }
    }

    // DTO para la respuesta
    public static class MesaResponse {
        private Integer id;
        private Integer numero;

        public MesaResponse(Integer id, Integer numero) {
            this.id = id;
            this.numero = numero;
        }

        public Integer getId() { return id; }
        public Integer getNumero() { return numero; }
    }

    public static class DetalleResponse {
        private String nombre;
        private Integer cantidad;
        private Double precio;

        public DetalleResponse(String nombre, Integer cantidad, Double precio) {
            this.nombre = nombre;
            this.cantidad = cantidad;
            this.precio = precio;
        }

        public String getNombre() { return nombre; }
        public Integer getCantidad() { return cantidad; }
        public Double getPrecio() { return precio; }
    }

    public static class PedidoResponse {
        private Integer id;
        private MesaResponse mesa;
        private String estado;
        private LocalDateTime fechaHora;
        private Double total;
        private List<DetalleResponse> detalles;

        public PedidoResponse(Integer id, MesaResponse mesa, String estado, LocalDateTime fechaHora, Double total, List<DetalleResponse> detalles) {
            this.id = id;
            this.mesa = mesa;
            this.estado = estado;
            this.fechaHora = fechaHora;
            this.total = total;
            this.detalles = detalles;
        }

        public Integer getId() { return id; }
        public MesaResponse getMesa() { return mesa; }
        public String getEstado() { return estado; }
        public LocalDateTime getFechaHora() { return fechaHora; }
        public Double getTotal() { return total; }
        public List<DetalleResponse> getDetalles() { return detalles; }
    }

    private PedidoResponse toPedidoResponse(Pedido p) {
        BigDecimal total = BigDecimal.ZERO;
        List<DetalleResponse> items = new ArrayList<>();
        if (p.getDetalles() != null) {
            for (DetallePedido d : p.getDetalles()) {
                if (d == null) continue;
                if (d.getPlato() != null && d.getPlato().getPrecio() != null) {
                    total = total.add(d.getPlato().getPrecio().multiply(BigDecimal.valueOf(d.getCantidad())));
                }
                String nombre = d.getPlato() != null ? d.getPlato().getNombre() : "";
                Double precio = d.getPlato() != null && d.getPlato().getPrecio() != null ? d.getPlato().getPrecio().doubleValue() : 0.0;
                items.add(new DetalleResponse(nombre, d.getCantidad(), precio));
            }
        }
        MesaResponse mesaResp = null;
        if (p.getMesa() != null) {
            Integer mesaId = p.getMesa().getId();
            Integer numero = p.getMesa().getNumero();
            mesaResp = new MesaResponse(mesaId, numero);
        }
        return new PedidoResponse(
                p.getId(),
                mesaResp,
                p.getEstado() != null ? p.getEstado().name() : null,
                p.getFechaHora(),
                total.doubleValue(),
                items
        );
    }

    @PostMapping
    @Transactional
    public ResponseEntity<PedidoResponse> crearPedido(@RequestBody CrearPedidoRequest request) {
        Mesa mesa = mesaRepository.findById(request.getMesaId()).orElse(null);
        if (mesa == null) {
            return ResponseEntity.badRequest().build();
        }

        Pedido pedido = new Pedido();
        pedido.setMesa(mesa);
        pedido.setFechaHora(LocalDateTime.now());
        pedido.setEstado(Pedido.Estado.nuevo);
        pedido.setModalidad(Pedido.Modalidad.mesa);

        String metodo = request.getMetodoPago();
        if (metodo != null && !metodo.isEmpty()) {
            pedido.setMetodoPago(Pedido.MetodoPago.valueOf(metodo));
        } else {
            pedido.setMetodoPago(Pedido.MetodoPago.tarjeta);
        }

        List<DetallePedido> detalles = new ArrayList<>();
        BigDecimal total = BigDecimal.ZERO;

        for (LineaRequest linea : request.getLineas()) {
            Plato plato = platoRepository.findById(linea.getPlatoId()).orElse(null);
            if (plato == null) {
                return ResponseEntity.badRequest().build();
            }
            DetallePedido detalle = new DetallePedido();
            detalle.setPedido(pedido);
            detalle.setPlato(plato);
            detalle.setCantidad(linea.getCantidad());
            detalles.add(detalle);
            total = total.add(plato.getPrecio().multiply(BigDecimal.valueOf(linea.getCantidad())));
        }

        pedido.setDetalles(detalles);
        Pedido saved = pedidoRepository.save(pedido);

        return ResponseEntity.status(201).body(toPedidoResponse(saved));
    }

    @GetMapping("/pendientes")
    @PreAuthorize("hasAnyRole('PERSONAL', 'ADMINISTRADOR')")
    @Transactional(readOnly = true)
    public List<PedidoResponse> getPendientes() {
        List<Pedido> nuevos = pedidoRepository.findByEstado(Pedido.Estado.nuevo);
        return nuevos.stream().map(this::toPedidoResponse).collect(Collectors.toList());
    }

    @GetMapping
    @PreAuthorize("hasRole('ADMINISTRADOR')")
    @Transactional(readOnly = true)
    public List<PedidoResponse> getAllPedidos() {
    	List<Pedido> pedidos = pedidoRepository.findAll();
    	return pedidos.stream().map(this::toPedidoResponse).collect(Collectors.toList());
    }
    
    @PutMapping("/{id}/leido")
    @PreAuthorize("hasAnyRole('PERSONAL', 'ADMINISTRADOR')")
    @Transactional
    public ResponseEntity<PedidoResponse> marcarLeido(@PathVariable Integer id) {
        try {
            return pedidoRepository.findById(id)
                    .map(pedido -> {
                        // Log estado antes
                        System.out.println("[DEBUG] Pedido antes de marcar leido: " + pedido);
                        pedido.setEstado(Pedido.Estado.en_preparacion);
                        // Ensure required fields are present to avoid DB constraint violations
                        if (pedido.getMetodoPago() == null) {
                            pedido.setMetodoPago(Pedido.MetodoPago.tarjeta);
                        }
                        if (pedido.getMesa() == null) {
                            // Missing mesa would cause DB constraint errors; log and return 500
                            System.err.println("[ERROR] Pedido id=" + pedido.getId() + " tiene mesa nula, abortando save");
                            throw new IllegalStateException("Pedido sin mesa");
                        }
                        Pedido saved = pedidoRepository.save(pedido);
                        // Log estado después
                        System.out.println("[DEBUG] Pedido después de marcar leido: " + saved);
                        return ResponseEntity.ok(toPedidoResponse(saved));
                    })
                    .orElseGet(() -> ResponseEntity.notFound().build());
        } catch (Exception e) {
            System.err.println("[ERROR] Error al marcar pedido como leido: " + e.getMessage());
            e.printStackTrace();
            return ResponseEntity.internalServerError().build();
        }
    }

    @PutMapping("/{id}/entregar")
    @PreAuthorize("hasAnyRole('PERSONAL', 'ADMINISTRADOR')")
    @Transactional
    public ResponseEntity<PedidoResponse> marcarEntregado(@PathVariable Integer id) {
        return pedidoRepository.findById(id)
                .map(pedido -> {
                    pedido.setEstado(Pedido.Estado.entregado);
                    pedidoRepository.save(pedido);
                    return ResponseEntity.ok(toPedidoResponse(pedido));
                })
                .orElseGet(() -> ResponseEntity.notFound().build());
    }
    
    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMINISTRADOR')")
    @Transactional
    public ResponseEntity<Void> eliminarPedido(@PathVariable Integer id) {
        if (!pedidoRepository.existsById(id)) {
            return ResponseEntity.notFound().build();
        }
        pedidoRepository.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}
