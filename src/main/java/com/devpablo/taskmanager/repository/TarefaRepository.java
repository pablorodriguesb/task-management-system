package com.devpablo.taskmanager.repository;

import com.devpablo.taskmanager.enums.StatusTarefa;
import com.devpablo.taskmanager.model.Tarefa;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;


@Repository
public interface TarefaRepository extends JpaRepository<Tarefa, Long> {

    @Query("SELECT t FROM Tarefa t WHERE t.responsavel.id = :usuarioId")
    List<Tarefa> buscarPorUsuarioId(@Param("usuarioId") Long usuarioId);

    @Query("SELECT t FROM Tarefa t WHERE t.status = :status")
    List<Tarefa> buscarPorStatus(@Param("status") StatusTarefa status);

    @Query("SELECT t FROM Tarefa t WHERE t.responsavel.id = :usuarioId AND t.status = :status")
    List<Tarefa> buscarPorUsuarioIdEStatus(
            @Param("usuarioId") Long usuarioId,
            @Param("status") StatusTarefa status
    );
}