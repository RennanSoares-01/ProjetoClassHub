package com.classhub.usuario.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

/** Cadastro público: sempre cria usuário com role ALUNO (ADR-005 em .ai/architecture.md). */
public record RegistroRequest(
        @NotBlank(message = "matrícula é obrigatória") String matricula,
        @NotBlank(message = "nome é obrigatório") String nome,
        @Email(message = "e-mail inválido") @NotBlank(message = "e-mail é obrigatório") String email,
        @NotBlank(message = "senha é obrigatória") String senha
) {
}
