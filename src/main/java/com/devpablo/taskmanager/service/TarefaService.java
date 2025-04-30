package com.devpablo.taskmanager.service;

import com.devpablo.taskmanager.enums.StatusTarefa;
import com.devpablo.taskmanager.model.Tarefa;
import com.devpablo.taskmanager.repository.TarefaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class TarefaService {

    private final TarefaRepository tarefaRepository;

    @Autowired
    public TarefaService(TarefaRepository tarefaRepository) {
        this.tarefaRepository = tarefaRepository;
    }

    // Método para buscar por status
    public List<Tarefa> buscarPorStatus(StatusTarefa status) {
        return tarefaRepository.buscarPorStatus(status);
    }

    // Método para buscar por usuário
    public List<Tarefa> buscarPorUsuario(Long usuarioId) {
        return tarefaRepository.buscarPorUsuarioId(usuarioId);
    }

    // Método para buscar por usuário e status
    public List<Tarefa> buscarPorUsuarioEStatus(Long usuarioId, StatusTarefa status) {
        return tarefaRepository.buscarPorUsuarioIdEStatus(usuarioId, status);
    }

    // Método para buscar todas as tarefas
    public List<Tarefa> buscarTodasTarefas() {
        return tarefaRepository.findAll();
    }

    // Método ordenado por ID
    public List<Tarefa> buscarTodasOrdenadasPorId() {
        return tarefaRepository.findAllByOrderByIdAsc();
    }

    // Método para buscar tarefa por ID
    public Optional<Tarefa> buscarPorId(Long id) {
        return tarefaRepository.findById(id);
    }

    // Método para salvar tarefa
    public Tarefa salvar(Tarefa tarefa) {
        if (tarefa.getUsuario() == null) { // Alterado de getResponsavel() para getUsuario()
            throw new RuntimeException("Tarefa precisa ter um usuário responsável");
        }

        LocalDateTime agora = LocalDateTime.now();

        if (tarefa.getId() == null) {
            tarefa.setDataCriacao(agora);
        } else {
            tarefa.setDataAtualizacao(agora);
        }
        return tarefaRepository.save(tarefa);
    }

    // Método para atualizar tarefa
    @Transactional
    public Tarefa atualizar(Long id, Tarefa tarefaAtualizada) {
        Tarefa tarefaExistente = tarefaRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Tarefa não encontrada"));

        // Atualiza campos básicos
        tarefaExistente.setTitulo(tarefaAtualizada.getTitulo());
        tarefaExistente.setDescricao(tarefaAtualizada.getDescricao());
        tarefaExistente.setStatus(tarefaAtualizada.getStatus());
        tarefaExistente.setPrioridade(tarefaAtualizada.getPrioridade());
        tarefaExistente.setDataVencimento(tarefaAtualizada.getDataVencimento());
        tarefaExistente.setDataAtualizacao(LocalDateTime.now());

        // Atualiza relacionamentos (CORRIGIDO PARA USUÁRIO)
        if (tarefaAtualizada.getUsuario() != null) { // Alterado de getResponsavel()
            tarefaExistente.setUsuario(tarefaAtualizada.getUsuario()); // Alterado de setResponsavel()
        }
        if (tarefaAtualizada.getCategoria() != null) {
            tarefaExistente.setCategoria(tarefaAtualizada.getCategoria());
        }

        return tarefaRepository.save(tarefaExistente);
    }

    // Método para excluir tarefa
    @Transactional
    public void excluir(Long id) {
        Tarefa tarefa = tarefaRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Tarefa não encontrada"));
        tarefaRepository.delete(tarefa);
    }

    // Método para concluir tarefa
    @Transactional
    public Tarefa concluir(Long id) {
        Tarefa tarefa = tarefaRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Tarefa não encontrada com Id: " + id));

        LocalDateTime agora = LocalDateTime.now();
        tarefa.setStatus(StatusTarefa.CONCLUIDA);
        tarefa.setDataConclusao(agora);
        tarefa.setDataAtualizacao(agora);

        return tarefaRepository.save(tarefa);
    }
}
