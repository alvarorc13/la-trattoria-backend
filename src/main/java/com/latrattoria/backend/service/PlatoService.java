package com.latrattoria.backend.service;

import com.latrattoria.backend.model.Plato;
import com.latrattoria.backend.model.Categoria;
import com.latrattoria.backend.repository.PlatoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class PlatoService {

    @Autowired
    private PlatoRepository platoRepository;

    public List<Plato> findAll() {
        return platoRepository.findAll();
    }

    public Optional<Plato> findById(Integer id) {
        return platoRepository.findById(id);
    }

    public List<Plato> findByDisponibilidad(Plato.Disponibilidad disponibilidad) {
        return platoRepository.findByDisponibilidad(disponibilidad);
    }

    public List<Plato> findByCategoria(Categoria categoria) {
        return platoRepository.findByCategoria(categoria);
    }

    public Plato save(Plato plato) {
        return platoRepository.save(plato);
    }

    public void deleteById(Integer id) {
        platoRepository.deleteById(id);
    }

    public boolean existsById(Integer id) {
        return platoRepository.existsById(id);
    }
}
