package com.classhub.turma.dto;

import jakarta.validation.constraints.NotBlank;

public record CriarTurmaRequest(@NotBlank(message = "nome é obrigatório") String nome) {
}
