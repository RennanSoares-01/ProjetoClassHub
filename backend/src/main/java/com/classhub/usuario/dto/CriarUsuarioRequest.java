package com.classhub.usuario.dto;

import com.classhub.usuario.Role;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

/** Criação de professor/admin — restrita a um administrador autenticado (ADR-005). */
public record CriarUsuarioRequest(
        @NotBlank(message = "matrícula é obrigatória") String matricula,
        @NotBlank(message = "nome é obrigatório") String nome,
        @Email(message = "e-mail inválido") @NotBlank(message = "e-mail é obrigatório") String email,
        @NotBlank(message = "senha é obrigatória") String senha,
        @NotNull(message = "role é obrigatório") Role role
) {
}
