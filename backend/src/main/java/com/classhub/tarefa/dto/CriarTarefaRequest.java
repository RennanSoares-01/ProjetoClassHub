package com.classhub.tarefa.dto;

import com.classhub.tarefa.TipoTarefa;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDateTime;

public record CriarTarefaRequest(
        @NotNull(message = "turmaId é obrigatório") Long turmaId,
        @NotBlank(message = "título é obrigatório") String titulo,
        String descricao,
        @NotNull(message = "tipo é obrigatório") TipoTarefa tipo,
        @NotNull(message = "dataLimite é obrigatória") LocalDateTime dataLimite
) {
}
