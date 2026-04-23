package com.latrattoria.backend.security;

import com.latrattoria.backend.model.UsuarioSistema;
import com.latrattoria.backend.repository.UsuarioSistemaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import java.util.Collection;
import java.util.Collections;

@Service
public class CustomUserDetailsService implements UserDetailsService {
    @Autowired
    private UsuarioSistemaRepository usuarioSistemaRepository;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        UsuarioSistema usuario = usuarioSistemaRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("Usuario no encontrado: " + email));
        return new org.springframework.security.core.userdetails.User(
                usuario.getEmail(),
                usuario.getPasswordHash(),
                usuario.getActivo(), true, true, true,
                getAuthorities(usuario.getRol().name())
        );
    }

    private Collection<? extends GrantedAuthority> getAuthorities(String rol) {
        return Collections.singletonList(new SimpleGrantedAuthority("ROLE_" + rol.toUpperCase()));
    }
}
