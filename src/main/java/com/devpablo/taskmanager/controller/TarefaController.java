package com.devpablo.taskmanager.controller;

import com.devpablo.taskmanager.dto.TarefaRequestDTO;
import com.devpablo.taskmanager.dto.TarefaResponseDTO;
import com.devpablo.taskmanager.model.Categoria;
import com.devpablo.taskmanager.model.Tarefa;
import com.devpablo.taskmanager.model.Usuario;
import com.devpablo.taskmanager.service.TarefaService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/tarefas")
public class TarefaController {

    private final TarefaService tarefaService;

    public TarefaController(TarefaService tarefaService) {
        this.tarefaService = tarefaService;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public TarefaResponseDTO criar(@Valid @RequestBody TarefaRequestDTO dto) {
        Tarefa novaTarefa = converterParaEntidade(dto);
        Tarefa salva = tarefaService.salvar(novaTarefa);
        return converterParaDTO(salva);
    }

    @GetMapping
    public List<TarefaResponseDTO> listarTodas() {
        return tarefaService.buscarTodasOrdenadasPorId()
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
    public TarefaResponseDTO atualizar(@PathVariable Long id, @Valid
                                       @RequestBody TarefaRequestDTO dto) {
        Tarefa tarefaAtualizada = converterParaEntidade(dto);
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

    // Metodos auxiliares
    private Tarefa converterParaEntidade(TarefaRequestDTO dto) {
        Tarefa tarefa = new Tarefa();
        tarefa.setTitulo((dto.titulo()));
        tarefa.setDescricao(dto.descricao());
        tarefa.setStatus(dto.status());
        tarefa.setPrioridade(dto.prioridade());
        tarefa.setDataVencimento(dto.dataVencimento());

        // objetos relacionados com ids
        if (dto.responsavelId() != null) {
            Usuario responsavel = new Usuario();
            responsavel.setId(dto.responsavelId());
            tarefa.setResponsavel(responsavel);
        }

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
                tarefa.getResponsavel() != null ? tarefa.getResponsavel().getId() : null,
                tarefa.getCategoria() != null ? tarefa.getCategoria().getId() : null
        );
    }
}
