package com.devpablo.taskmanager.service;

import com.devpablo.taskmanager.model.Usuario;
import com.devpablo.taskmanager.repository.UsuarioRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class UsuarioService {

    private final UsuarioRepository usuarioRepository;

    @Autowired
    public UsuarioService(UsuarioRepository usuarioRepository) {
        this.usuarioRepository = usuarioRepository;
    }
    // metodo para buscar todos os usuarios
    public List<Usuario> buscarTodosUsuarios() {
        return usuarioRepository.findAll();
    }

    public Optional<Usuario> buscarPorId(Long id) {
        return usuarioRepository.findById(id);
    }

    public Usuario salvar(Usuario usuario) {
        validarEmailUnico(usuario);
        // verificando se é um novo registro ou atualizacao
        if(usuario.getId() == null) {
            usuario.setDataCriacao(LocalDateTime.now()); // novos usuarios recebem dataCriacao
        } else {
            usuario.setDataAtualizacao(LocalDateTime.now()); // novos usuarios recebem dataAtualizacao
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
        if (usuarioRepository.existePorEmail(usuario.getEmail())) {
            throw new RuntimeException("Já existe usuário com este e-mail");

        }
    }
}
