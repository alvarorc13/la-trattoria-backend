package com.latrattoria.backend.service;

import com.latrattoria.backend.model.Categoria;
import com.latrattoria.backend.repository.CategoriaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class CategoriaService {

    @Autowired
    private CategoriaRepository categoriaRepository;

    public List<Categoria> findAll() {
        return categoriaRepository.findAll();
    }

    public Optional<Categoria> findById(Integer id) {
        return categoriaRepository.findById(id);
    }

    public Categoria save(Categoria categoria) {
        return categoriaRepository.save(categoria);
    }

    public boolean existsByNombre(String nombre) {
        return categoriaRepository.existsByNombre(nombre);
    }

    public void deleteById(Integer id) {
        categoriaRepository.deleteById(id);
    }

    public boolean existsById(Integer id) {
        return categoriaRepository.existsById(id);
    }
}
