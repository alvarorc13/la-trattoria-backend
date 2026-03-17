package com.latrattoria.backend.service;

import com.latrattoria.backend.model.Mesa;
import com.latrattoria.backend.model.Pedido;
import com.latrattoria.backend.repository.PedidoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class PedidoService {

    @Autowired
    private PedidoRepository pedidoRepository;

    public List<Pedido> findAll() {
        return pedidoRepository.findAll();
    }

    public Optional<Pedido> findById(Integer id) {
        return pedidoRepository.findById(id);
    }

    public List<Pedido> findByMesa(Mesa mesa) {
        return pedidoRepository.findByMesa(mesa);
    }

    public List<Pedido> findByEstado(Pedido.Estado estado) {
        return pedidoRepository.findByEstado(estado);
    }

    public List<Pedido> findByMesaAndEstado(Mesa mesa, Pedido.Estado estado) {
        return pedidoRepository.findByMesaAndEstado(mesa, estado);
    }

    public Pedido save(Pedido pedido) {
        return pedidoRepository.save(pedido);
    }

    public void deleteById(Integer id) {
        pedidoRepository.deleteById(id);
    }

    public boolean existsById(Integer id) {
        return pedidoRepository.existsById(id);
    }
}
