package com.devpablo.taskmanager.dto;

import java.time.LocalDateTime;

public record CategoriaResponseDTO(
    Long id,
    String nome,
    String descricao,
    LocalDateTime dataCriacao
    ) {}
