package com.classhub.usuario.dto;

import jakarta.validation.constraints.NotBlank;

public record LoginRequest(
        @NotBlank(message = "matrícula é obrigatória") String matricula,
        @NotBlank(message = "senha é obrigatória") String senha
) {
}
