package com.classhub.tarefa.dto;

import com.classhub.tarefa.TipoTarefa;

import java.time.Instant;
import java.time.LocalDateTime;

public record TarefaResponse(
        Long id,
        Long turmaId,
        String turmaNome,
        String titulo,
        String descricao,
        TipoTarefa tipo,
        LocalDateTime dataLimite,
        boolean aberta,
        Instant criadoEm
) {
}
