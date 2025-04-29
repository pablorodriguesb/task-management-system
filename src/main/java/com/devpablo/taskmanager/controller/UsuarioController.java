package com.devpablo.taskmanager.controller;

import com.devpablo.taskmanager.dto.UsuarioRequestDTO;
import com.devpablo.taskmanager.dto.UsuarioResponseDTO;
import com.devpablo.taskmanager.model.Usuario;
import com.devpablo.taskmanager.service.UsuarioService;
import jakarta.validation.Valid;
import org.apache.coyote.Response;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;
import java.util.Map;

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
    public ResponseEntity<?> criar(@Valid @RequestBody UsuarioRequestDTO dto) {
        try {
            // conversao simples do DTO pra Entidade
            Usuario usuario = new Usuario();
            usuario.setNome(dto.nome());
            usuario.setEmail(dto.email());
            usuario.setSenha(dto.senha());

            Usuario salvo = usuarioService.salvar(usuario);
            return ResponseEntity.status(HttpStatus.CREATED).body(converterParaResponseDTO(salvo));
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.CONFLICT)
                    .body(Map.of("mensagem", e.getMessage()));
        }
    }


    @PutMapping("/{id}")
    public UsuarioResponseDTO atualizar(@PathVariable Long id, @Valid
                                        @RequestBody UsuarioRequestDTO dto) {
        Usuario usuario = converterParaEntidade(dto);
        usuario.setId(id);
        Usuario atualizado = usuarioService.salvar(usuario);
        return converterParaResponseDTO(atualizado);
    }

    // Endpoint para listar todos os usuarios
    @GetMapping
    public List<UsuarioResponseDTO> listarTodos() {
        return usuarioService.buscarTodosUsuarios()
                .stream()
                .map(this::converterParaResponseDTO)
                .toList();
    }

    // Endpoint para buscar o usuario atual
    @GetMapping("/atual")
    public ResponseEntity <UsuarioResponseDTO> getUsuarioAtual (Principal principal) {
        if (principal == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
        try {
            String email = principal.getName();
            Usuario usuario = usuarioService.buscarPorEmail(email)
                    .orElseThrow(() -> new RuntimeException("Usuário não encontrado"));
            return ResponseEntity.ok(converterParaResponseDTO(usuario));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    // Metodo auxiliar para converter entidade em DTO de resposta
    private UsuarioResponseDTO converterParaResponseDTO(Usuario usuario) {
        return new UsuarioResponseDTO(
                usuario.getId(),
                usuario.getNome(),
                usuario.getEmail(),
                usuario.getDataCriacao());
    }

    private Usuario converterParaEntidade(UsuarioRequestDTO dto) {
        Usuario usuario = new Usuario();
        usuario.setNome(dto.nome());
        usuario.setEmail(dto.email());
        usuario.setSenha(dto.senha());
        return usuario;
    }
}
