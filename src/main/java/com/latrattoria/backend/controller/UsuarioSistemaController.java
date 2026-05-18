package com.latrattoria.backend.controller;

import com.latrattoria.backend.model.UsuarioSistema;
import com.latrattoria.backend.repository.UsuarioSistemaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/v1/usuarios")
public class UsuarioSistemaController {
    @Autowired
    private UsuarioSistemaRepository usuarioRepo;

    @Autowired
    private PasswordEncoder passwordEncoder;

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
        usuario.setPasswordHash(passwordEncoder.encode(usuario.getPasswordHash()));
        return usuarioRepo.save(usuario);
    }

    @PutMapping("/{id}")
    public ResponseEntity<UsuarioSistema> updateUsuario(@PathVariable Integer id, @RequestBody UsuarioSistema usuario) {
        Optional<UsuarioSistema> usuarioActualOpt = usuarioRepo.findById(id);
        if (usuarioActualOpt.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        UsuarioSistema usuarioActual = usuarioActualOpt.get();
        usuarioActual.setNombre(usuario.getNombre());
        usuarioActual.setEmail(usuario.getEmail());
        usuarioActual.setRol(usuario.getRol());
        usuarioActual.setActivo(usuario.getActivo());
        // Solo actualiza el password si viene en el request
        if (usuario.getPasswordHash() != null) {
            usuarioActual.setPasswordHash(usuario.getPasswordHash());
        }
        return ResponseEntity.ok(usuarioRepo.save(usuarioActual));
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