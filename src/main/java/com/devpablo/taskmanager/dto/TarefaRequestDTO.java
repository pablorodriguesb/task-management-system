package com.devpablo.taskmanager.dto;

import com.devpablo.taskmanager.enums.PrioridadeTarefa;
import com.devpablo.taskmanager.enums.StatusTarefa;
import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDateTime;

public record TarefaRequestDTO (
    @NotBlank(message = "Titulo é obrigatório")
    String titulo,

    String descricao,

    @NotNull(message = "Status é obrigatorio")
    StatusTarefa status,

    PrioridadeTarefa prioridade,

    @FutureOrPresent(message = "Data de vencimento deve ser no presente ou futuro")
    LocalDateTime dataVencimento,

    Long categoriaId
    ){}
