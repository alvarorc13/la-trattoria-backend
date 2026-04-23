package com.latrattoria.backend.controller;

import com.latrattoria.backend.model.UsuarioSistema;
import com.latrattoria.backend.repository.UsuarioSistemaRepository;
import com.latrattoria.backend.security.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;
import java.util.Map;

@RestController
@RequestMapping("/api/v1/auth")
public class AuthController {
    @Autowired
    private UsuarioSistemaRepository usuarioSistemaRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private JwtUtil jwtUtil;

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody Map<String, String> loginData) {
        String email = loginData.get("email");
        String password = loginData.get("password");
        if (email == null || password == null) {
            return ResponseEntity.badRequest().body(Map.of("mensaje", "Faltan campos obligatorios"));
        }
        UsuarioSistema usuario = usuarioSistemaRepository.findByEmail(email).orElse(null);
        if (usuario == null || !usuario.getActivo() || !passwordEncoder.matches(password, usuario.getPasswordHash())) {
            return ResponseEntity.status(401).body(Map.of("mensaje", "Credenciales incorrectas"));
        }
        String token = jwtUtil.generateToken(usuario.getEmail(), usuario.getRol().name(), usuario.getNombre());
        return ResponseEntity.ok(Map.of(
                "token", token,
                "rol", usuario.getRol().name(),
                "nombre", usuario.getNombre()
        ));
    }
}
