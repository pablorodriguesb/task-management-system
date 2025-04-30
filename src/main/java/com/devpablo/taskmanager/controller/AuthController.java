package com.devpablo.taskmanager.controller;

import com.devpablo.taskmanager.dto.LoginRequestDTO;
import com.devpablo.taskmanager.model.Usuario;
import com.devpablo.taskmanager.repository.UsuarioRepository;
import com.devpablo.taskmanager.security.JwtUtils;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/auth")
public class AuthController {

    private final UsuarioRepository usuarioRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtils jwtUtils;

    public AuthController(UsuarioRepository usuarioRepository, PasswordEncoder passwordEncoder, JwtUtils jwtUtils) {
        this.usuarioRepository = usuarioRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtUtils = jwtUtils;
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequestDTO dto) {
        Usuario usuario = usuarioRepository.buscarPorEmail(dto.email())
                .orElse(null);

        if (usuario == null || !passwordEncoder.matches(dto.senha(), usuario.getSenha())) {
            return ResponseEntity.status(401).body(Map.of("mensagem", "Credenciais inválidas"));
        }

        String token = jwtUtils.generateToken(usuario.getEmail());
        return ResponseEntity.ok(Map.of("token", token));
    }
}
