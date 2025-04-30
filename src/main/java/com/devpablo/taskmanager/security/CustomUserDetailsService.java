package com.devpablo.taskmanager.security;

import com.devpablo.taskmanager.model.Usuario;
import com.devpablo.taskmanager.repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class CustomUserDetailsService implements UserDetailsService {

    @Autowired
    private final UsuarioRepository usuarioRepository;

    @Autowired
    public CustomUserDetailsService(UsuarioRepository usuarioRepository) {
        this.usuarioRepository = usuarioRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        System.out.println("Tentando autenticar com email: " + email);
        Usuario usuario = usuarioRepository.buscarPorEmail(email)
                .orElseThrow(() -> {
                    System.out.println("Usuário não encontrado para: " + email);
                    return new UsernameNotFoundException("Usuário não encontrado com email: " + email);
                });
        System.out.println("Usuário encontrado: " + usuario.getEmail());
        return org.springframework.security.core.userdetails.User
                .withUsername(usuario.getEmail())
                .password(usuario.getSenha())
                .roles("USER")
                .build();
    }
}