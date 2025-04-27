package com.devpablo.taskmanager.dto;

import com.devpablo.taskmanager.enums.PrioridadeTarefa;
import com.devpablo.taskmanager.enums.StatusTarefa;

import java.time.LocalDateTime;

public record TarefaResponseDTO(
        Long id,
        String titulo,
        String descricao,
        StatusTarefa status,
        PrioridadeTarefa prioridade,
        LocalDateTime dataCriacao,
        LocalDateTime dataConclusao,
        LocalDateTime dataVencimento,
        Long responsavelId,
        Long categoriaId
) {}
