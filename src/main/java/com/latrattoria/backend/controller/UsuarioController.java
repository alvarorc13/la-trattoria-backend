package com.latrattoria.backend.controller;

import com.latrattoria.backend.model.UsuarioSistema;
import com.latrattoria.backend.service.UsuarioSistemaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/v1/usuarios")
@PreAuthorize("hasRole('ADMINISTRADOR')")
public class UsuarioController {

    @Autowired
    private UsuarioSistemaService usuarioService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @GetMapping
    public List<Map<String, Object>> getAll() {
        return usuarioService.findAll().stream()
                .map(this::toResponse)
                .toList();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Map<String, Object>> getById(@PathVariable Integer id) {
        return usuarioService.findById(id)
                .map(u -> ResponseEntity.ok(toResponse(u)))
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<?> create(@RequestBody Map<String, String> data) {
        String nombre = data.get("nombre");
        String email = data.get("email");
        String password = data.get("password");
        String rol = data.get("rol");

        if (nombre == null || email == null || password == null || rol == null) {
            return ResponseEntity.badRequest().body(Map.of("mensaje", "Faltan campos obligatorios"));
        }

        if (usuarioService.findByEmail(email).isPresent()) {
            return ResponseEntity.status(409).body(Map.of("mensaje", "Ya existe un usuario con ese email"));
        }

        UsuarioSistema usuario = new UsuarioSistema();
        usuario.setNombre(nombre);
        usuario.setEmail(email);
        usuario.setPasswordHash(password);
        usuario.setRol(UsuarioSistema.Rol.valueOf(rol));
        usuario.setActivo(true);

        UsuarioSistema saved = usuarioService.save(usuario);
        return ResponseEntity.status(201).body(toResponse(saved));
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> update(@PathVariable Integer id, @RequestBody Map<String, String> data) {
        return usuarioService.findById(id)
                .map(existing -> {
                    if (data.containsKey("nombre")) existing.setNombre(data.get("nombre"));
                    if (data.containsKey("email")) existing.setEmail(data.get("email"));
                    if (data.containsKey("rol")) existing.setRol(UsuarioSistema.Rol.valueOf(data.get("rol")));
                    if (data.containsKey("password") && !data.get("password").isBlank()) {
                        existing.setPasswordHash(passwordEncoder.encode(data.get("password")));
                    }
                    UsuarioSistema saved = usuarioService.save(existing);
                    return ResponseEntity.ok(toResponse(saved));
                })
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Integer id) {
        if (!usuarioService.existsById(id)) {
            return ResponseEntity.notFound().build();
        }
        usuarioService.deleteById(id);
        return ResponseEntity.noContent().build();
    }

    private Map<String, Object> toResponse(UsuarioSistema u) {
        return Map.of(
                "id", u.getId(),
                "nombre", u.getNombre(),
                "email", u.getEmail(),
                "rol", u.getRol().name(),
                "activo", u.getActivo()
        );
    }
}
