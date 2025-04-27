package com.devpablo.taskmanager.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record UsuarioRequestDTO(
    @NotBlank(message = "Nome é obrigatório")
    String nome,

    @Email(message = "E-mail inválido")
    @NotBlank(message = "E-mail é obrigatorio")
    String email,

    @Size(min = 8, message = "Senha deve ter no mínimo 8 caracteres")
    String senha
) {}
