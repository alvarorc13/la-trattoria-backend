package com.latrattoria.backend.controller;

import com.latrattoria.backend.model.Plato;
import com.latrattoria.backend.model.Categoria;
import com.latrattoria.backend.repository.PlatoRepository;
import com.latrattoria.backend.repository.CategoriaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/v1/platos")
public class PlatoController {
    @Autowired
    private PlatoRepository platoRepository;
    @Autowired
    private CategoriaRepository categoriaRepository;

    @GetMapping
    public List<Plato> getAllPlatosActivos() {
        return platoRepository.findByDisponibilidad(Plato.Disponibilidad.activo);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Plato> getPlatoById(@PathVariable Integer id) {
        Optional<Plato> plato = platoRepository.findById(id);
        return plato.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @GetMapping("/categoria/{idCat}")
    public ResponseEntity<List<Plato>> getPlatosByCategoria(@PathVariable Integer idCat) {
        Optional<Categoria> categoria = categoriaRepository.findById(idCat);
        if (categoria.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        List<Plato> platos = platoRepository.findByCategoria(categoria.get());
        return ResponseEntity.ok(platos);
    }

    @PostMapping
    @PreAuthorize("hasRole('ADMINISTRADOR')")
    public ResponseEntity<Plato> createPlato(@RequestBody Plato plato) {
        Plato saved = platoRepository.save(plato);
        return ResponseEntity.status(201).body(saved);
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMINISTRADOR')")
    public ResponseEntity<Plato> updatePlato(@PathVariable Integer id, @RequestBody Plato plato) {
        return platoRepository.findById(id)
                .map(existing -> {
                    existing.setNombre(plato.getNombre());
                    existing.setDescripcion(plato.getDescripcion());
                    existing.setPrecio(plato.getPrecio());
                    existing.setImagen(plato.getImagen());
                    existing.setDisponibilidad(plato.getDisponibilidad());
                    existing.setCategoria(plato.getCategoria());
                    platoRepository.save(existing);
                    return ResponseEntity.ok(existing);
                })
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMINISTRADOR')")
    public ResponseEntity<Void> deletePlato(@PathVariable Integer id) {
        if (!platoRepository.existsById(id)) {
            return ResponseEntity.notFound().build();
        }
        platoRepository.deleteById(id);
        return ResponseEntity.noContent().build();
    }
    // Endpoints POST, PUT, DELETE protegidos por rol admin (añadir seguridad después)
}
