package com.devpablo.taskmanager.repository;

import com.devpablo.taskmanager.enums.StatusTarefa;
import com.devpablo.taskmanager.model.Tarefa;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;


@Repository
public interface TarefaRepository extends JpaRepository<Tarefa, Long> {
    // Busca tarefas pelo ID de usuário
    List<Tarefa> buscarPorUsuarioId(Long usuarioId);

    // Busca tarefas pelo status
    List<Tarefa> buscarPorStatus(StatusTarefa status);

    // Busca tarefas pelo ID do usuario e status
    List<Tarefa> buscarPorUsuarioIdEStatus(Long usuarioId, StatusTarefa status);
}
