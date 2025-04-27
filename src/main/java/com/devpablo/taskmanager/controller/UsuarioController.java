package com.devpablo.taskmanager.controller;

import com.devpablo.taskmanager.dto.UsuarioRequestDTO;
import com.devpablo.taskmanager.dto.UsuarioResponseDTO;
import com.devpablo.taskmanager.model.Usuario;
import com.devpablo.taskmanager.service.UsuarioService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/usuarios")
public class UsuarioController {

    private final UsuarioService usuarioService;

    // injecao de dependencia por construtor
    public UsuarioController(UsuarioService usuarioService) {
        this.usuarioService = usuarioService;
    }

    // Endpoint para criar usuario
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public UsuarioResponseDTO criar(@Valid @RequestBody UsuarioRequestDTO dto) {
        // conversao simples do DTO pra Entidade
        Usuario usuario = new Usuario();
        usuario.setNome(dto.nome());
        usuario.setEmail(dto.email());
        usuario.setSenha(dto.senha());

        Usuario salvo = usuarioService.salvar(usuario);
        return converterParaResponseDTO(salvo);
    }

    // Endpoint para listar todos os usuarios
    @GetMapping
    public List<UsuarioResponseDTO> listarTodos() {
        return usuarioService.buscarTodosUsuarios()
                .stream()
                .map(this::converterParaResponseDTO)
                .toList();
    }

    // Metodo auxiliar para converter entidade em DTO de resposta
    private UsuarioResponseDTO converterParaResponseDTO(Usuario usuario) {
        return new UsuarioResponseDTO(
                usuario.getId(),
                usuario.getNome(),
                usuario.getEmail(),
                usuario.getDataCriacao());
    }
}
