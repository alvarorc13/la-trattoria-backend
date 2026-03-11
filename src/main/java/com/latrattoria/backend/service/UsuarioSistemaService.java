package com.latrattoria.backend.service;

import com.latrattoria.backend.model.UsuarioSistema;
import com.latrattoria.backend.repository.UsuarioSistemaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UsuarioSistemaService {

    @Autowired
    private UsuarioSistemaRepository usuarioSistemaRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    public List<UsuarioSistema> findAll() {
        return usuarioSistemaRepository.findAll();
    }

    public Optional<UsuarioSistema> findById(Integer id) {
        return usuarioSistemaRepository.findById(id);
    }

    public Optional<UsuarioSistema> findByEmail(String email) {
        return usuarioSistemaRepository.findByEmail(email);
    }

    public UsuarioSistema save(UsuarioSistema usuario) {
        if (usuario.getId() == null) {
            usuario.setPasswordHash(passwordEncoder.encode(usuario.getPasswordHash()));
        }
        return usuarioSistemaRepository.save(usuario);
    }

    public void deleteById(Integer id) {
        usuarioSistemaRepository.deleteById(id);
    }

    public boolean existsById(Integer id) {
        return usuarioSistemaRepository.existsById(id);
    }
}
