package com.latrattoria.backend.repository;

import com.latrattoria.backend.model.Notificacion;
import com.latrattoria.backend.model.Pedido;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface NotificacionRepository extends JpaRepository<Notificacion, Integer> {
    List<Notificacion> findByPedido(Pedido pedido);
    List<Notificacion> findByLeidaFalse();
}
