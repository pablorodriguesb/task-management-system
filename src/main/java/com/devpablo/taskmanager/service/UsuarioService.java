package com.devpablo.taskmanager.service;

import com.devpablo.taskmanager.model.Usuario;
import com.devpablo.taskmanager.repository.UsuarioRepository;

import com.devpablo.taskmanager.security.CustomUserDetailsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class UsuarioService {

    private final UsuarioRepository usuarioRepository;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public UsuarioService(UsuarioRepository usuarioRepository, PasswordEncoder passwordEncoder) {
        this.usuarioRepository = usuarioRepository;
        this.passwordEncoder = passwordEncoder;
    }
    // metodo para buscar todos os usuarios
    public List<Usuario> buscarTodosUsuarios() {
        return usuarioRepository.findAll();
    }

    public Optional<Usuario> buscarPorId(Long id) {
        return usuarioRepository.findById(id);
    }

    public Optional<Usuario> buscarPorEmail(String email) {
        return usuarioRepository.buscarPorEmail(email);
    }

    public Usuario salvar(Usuario usuario) {
        validarEmailUnico(usuario);

        // Codificar a senha antes de salvar
        if (usuario.getSenha() != null) {
            usuario.setSenha(passwordEncoder.encode(usuario.getSenha()));
        }

        // verificando se é um novo registro ou atualizacao
        if(usuario.getId() == null) {
            usuario.setDataCriacao(LocalDateTime.now()); // novos usuarios recebem dataCriacao
        } else {
            usuario.setDataAtualizacao(LocalDateTime.now()); // atualizações recebem dataAtualizacao
        }
        return usuarioRepository.save(usuario);
    }

    @Transactional
    public void excluirPorId(Long id) {
        Usuario usuario = usuarioRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Usuário não encontrado" +
                "com ID: " + id));
            usuarioRepository.delete(usuario);
    }

    // validacao de email único antes de salvar
    private void validarEmailUnico(Usuario usuario) {
        String emailNormalizado = usuario.getEmail().toLowerCase(); // Normaliza para lowercase
        Optional<Usuario> existente = usuarioRepository.buscarPorEmail(emailNormalizado);

        // Se existir um usuário com o email E o ID for diferente (ou novo cadastro)
        if (existente.isPresent() && (usuario.getId() == null || !existente.get().getId().equals(usuario.getId()))) {
            throw new RuntimeException("Já existe usuário com este e-mail");
        }
    }
}

