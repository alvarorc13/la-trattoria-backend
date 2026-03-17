package com.latrattoria.backend.service;

import com.latrattoria.backend.model.DetallePedido;
import com.latrattoria.backend.model.Pedido;
import com.latrattoria.backend.repository.DetallePedidoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class DetallePedidoService {

    @Autowired
    private DetallePedidoRepository detallePedidoRepository;

    public List<DetallePedido> findAll() {
        return detallePedidoRepository.findAll();
    }

    public Optional<DetallePedido> findById(Integer id) {
        return detallePedidoRepository.findById(id);
    }

    public List<DetallePedido> findByPedido(Pedido pedido) {
        return detallePedidoRepository.findByPedido(pedido);
    }

    public DetallePedido save(DetallePedido detalle) {
        return detallePedidoRepository.save(detalle);
    }

    public void deleteById(Integer id) {
        detallePedidoRepository.deleteById(id);
    }

    public boolean existsById(Integer id) {
        return detallePedidoRepository.existsById(id);
    }
}
