package com.latrattoria.backend.repository;

import com.latrattoria.backend.model.Plato;
import com.latrattoria.backend.model.Categoria;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface PlatoRepository extends JpaRepository<Plato, Integer> {
    List<Plato> findByDisponibilidad(Plato.Disponibilidad disponibilidad);
    List<Plato> findByCategoria(Categoria categoria);
}
