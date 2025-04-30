package com.devpablo.taskmanager.controller;

import com.devpablo.taskmanager.dto.TarefaRequestDTO;
import com.devpablo.taskmanager.dto.TarefaResponseDTO;
import com.devpablo.taskmanager.model.Categoria;
import com.devpablo.taskmanager.model.Tarefa;
import com.devpablo.taskmanager.model.Usuario;
import com.devpablo.taskmanager.repository.UsuarioRepository;
import com.devpablo.taskmanager.service.TarefaService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/tarefas")
public class TarefaController {

    private final TarefaService tarefaService;
    private final UsuarioRepository usuarioRepository;

    public TarefaController(TarefaService tarefaService, UsuarioRepository usuarioRepository) {
        this.tarefaService = tarefaService;
        this.usuarioRepository = usuarioRepository;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public TarefaResponseDTO criar(
            @Valid @RequestBody TarefaRequestDTO dto,
            Authentication authentication
    ) {
        Usuario usuario = getUsuarioAutenticado(authentication);
        Tarefa novaTarefa = converterParaEntidade(dto, usuario);
        Tarefa salva = tarefaService.salvar(novaTarefa);
        return converterParaDTO(salva);
    }

    @GetMapping
    public List<TarefaResponseDTO> listarDoUsuario(Authentication authentication) {
        Usuario usuario = getUsuarioAutenticado(authentication);
        return tarefaService.buscarPorUsuario(usuario.getId())
                .stream()
                .map(this::converterParaDTO)
                .toList();
    }

    @GetMapping("/{id}")
    public TarefaResponseDTO buscarPorId(@PathVariable Long id) {
        Tarefa tarefa = tarefaService.buscarPorId(id)
                .orElseThrow(() -> new RuntimeException("Tarefa não encontrada"));
        return converterParaDTO(tarefa);
    }

    @PutMapping("/{id}")
    public TarefaResponseDTO atualizar(
            @PathVariable Long id,
            @Valid @RequestBody TarefaRequestDTO dto,
            Authentication authentication
    ) {
        Usuario usuario = getUsuarioAutenticado(authentication);
        Tarefa tarefaAtualizada = converterParaEntidade(dto, usuario);
        Tarefa salva = tarefaService.atualizar(id, tarefaAtualizada);
        return converterParaDTO(salva);
    }

    @PutMapping("/{id}/concluir")
    public TarefaResponseDTO concluir(@PathVariable Long id) {
        Tarefa tarefa = tarefaService.concluir(id);
        return converterParaDTO(tarefa);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void excluir(@PathVariable Long id) {
        tarefaService.excluir(id);
    }

    private Usuario getUsuarioAutenticado(Authentication authentication) {
        String email = authentication.getName();
        return usuarioRepository.buscarPorEmail(email)
                .orElseThrow(() -> new RuntimeException("Usuário não encontrado"));
    }

    private Tarefa converterParaEntidade(TarefaRequestDTO dto, Usuario usuario) {
        Tarefa tarefa = new Tarefa();
        tarefa.setTitulo(dto.titulo());
        tarefa.setDescricao(dto.descricao());
        tarefa.setStatus(dto.status());
        tarefa.setPrioridade(dto.prioridade());
        tarefa.setDataVencimento(dto.dataVencimento());
        tarefa.setUsuario(usuario);

        if (dto.categoriaId() != null) {
            Categoria categoria = new Categoria();
            categoria.setId(dto.categoriaId());
            tarefa.setCategoria(categoria);
        }

        return tarefa;
    }

    private TarefaResponseDTO converterParaDTO(Tarefa tarefa) {
        return new TarefaResponseDTO(
                tarefa.getId(),
                tarefa.getTitulo(),
                tarefa.getDescricao(),
                tarefa.getStatus(),
                tarefa.getPrioridade(),
                tarefa.getDataCriacao(),
                tarefa.getDataConclusao(),
                tarefa.getDataVencimento(),
                tarefa.getUsuario().getId(),
                tarefa.getCategoria() != null ? tarefa.getCategoria().getId() : null
        );
    }
}
