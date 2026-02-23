package com.latrattoria.backend.repository;

import com.latrattoria.backend.model.Mesa;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface MesaRepository extends JpaRepository<Mesa, Integer> {
    Optional<Mesa> findByNumero(Integer numero);
    Optional<Mesa> findByCodigoQr(String codigoQr);
}
