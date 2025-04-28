package com.devpablo.taskmanager.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record CategoriaRequestDTO (
        @NotBlank(message = "Nome é obrigatório")
        String nome,

        @Size(max = 500, message = "Descrição deve ter no máximo 500 caracteres")
        String descricao
) {}

