package com.latrattoria.backend.controller;

import com.latrattoria.backend.model.Categoria;
import com.latrattoria.backend.repository.CategoriaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.Optional;
import org.springframework.security.access.prepost.PreAuthorize;

@RestController
@RequestMapping("/api/v1/categorias")
public class CategoriaController {
    @Autowired
    private CategoriaRepository categoriaRepository;

    @GetMapping
    public List<Categoria> getAllCategorias() {
        return categoriaRepository.findAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Categoria> getCategoriaById(@PathVariable Integer id) {
        Optional<Categoria> categoria = categoriaRepository.findById(id);
        return categoria.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping
    @PreAuthorize("hasRole('ADMINISTRADOR')")
    public ResponseEntity<Categoria> createCategoria(@RequestBody Categoria categoria) {
        Categoria.NombreCategoria nombreEnum = categoria.getNombre();
        if (nombreEnum == null) {
            return ResponseEntity.badRequest().build();
        }
        if (categoriaRepository.existsByNombre(nombreEnum)) {
            return ResponseEntity.status(409).build();
        }
        categoria.setNombre(nombreEnum);
        Categoria saved = categoriaRepository.save(categoria);
        return ResponseEntity.status(201).body(saved);
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMINISTRADOR')")
    public ResponseEntity<Categoria> updateCategoria(@PathVariable Integer id, @RequestBody Categoria categoria) {
        Categoria.NombreCategoria nombreEnum = categoria.getNombre();
        if (nombreEnum == null) {
            return ResponseEntity.badRequest().build();
        }
        return categoriaRepository.findById(id)
                .map(existing -> {
                    existing.setNombre(nombreEnum);
                    categoriaRepository.save(existing);
                    return ResponseEntity.ok(existing);
                })
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMINISTRADOR')")
    public ResponseEntity<Void> deleteCategoria(@PathVariable Integer id) {
        if (!categoriaRepository.existsById(id)) {
            return ResponseEntity.notFound().build();
        }
        categoriaRepository.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}
