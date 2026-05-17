package com.latrattoria.backend.controller;

import com.latrattoria.backend.model.UsuarioSistema;
import com.latrattoria.backend.repository.UsuarioSistemaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/v1/usuarios")
public class UsuarioSistemaController {
    @Autowired
    private UsuarioSistemaRepository usuarioRepo;

    @GetMapping("/admin/todos")
    public List<UsuarioSistema> getAllUsuarios() {
        return usuarioRepo.findAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<UsuarioSistema> getUsuarioById(@PathVariable Integer id) {
        Optional<UsuarioSistema> usuario = usuarioRepo.findById(id);
        return usuario.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping
    public UsuarioSistema createUsuario(@RequestBody UsuarioSistema usuario) {
        return usuarioRepo.save(usuario);
    }

    @PutMapping("/{id}")
    public ResponseEntity<UsuarioSistema> updateUsuario(@PathVariable Integer id, @RequestBody UsuarioSistema usuario) {
        if (!usuarioRepo.existsById(id)) {
            return ResponseEntity.notFound().build();
        }
        usuario.setId(id);
        return ResponseEntity.ok(usuarioRepo.save(usuario));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteUsuario(@PathVariable Integer id) {
        if (!usuarioRepo.existsById(id)) {
            return ResponseEntity.notFound().build();
        }
        usuarioRepo.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}
