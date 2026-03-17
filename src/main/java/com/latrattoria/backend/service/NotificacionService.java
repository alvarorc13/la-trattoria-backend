package com.latrattoria.backend.service;

import com.latrattoria.backend.model.Notificacion;
import com.latrattoria.backend.model.Pedido;
import com.latrattoria.backend.repository.NotificacionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class NotificacionService {

    @Autowired
    private NotificacionRepository notificacionRepository;

    public List<Notificacion> findAll() {
        return notificacionRepository.findAll();
    }

    public Optional<Notificacion> findById(Integer id) {
        return notificacionRepository.findById(id);
    }

    public List<Notificacion> findByPedido(Pedido pedido) {
        return notificacionRepository.findByPedido(pedido);
    }

    public List<Notificacion> findNoLeidas() {
        return notificacionRepository.findByLeidaFalse();
    }

    public Notificacion save(Notificacion notificacion) {
        return notificacionRepository.save(notificacion);
    }

    public void deleteById(Integer id) {
        notificacionRepository.deleteById(id);
    }

    public boolean existsById(Integer id) {
        return notificacionRepository.existsById(id);
    }
}
